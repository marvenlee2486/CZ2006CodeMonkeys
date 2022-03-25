import boto3
import datetime
import json
from http import HTTPStatus
from shared.validation_schema import UserSchema
import os

USER_TABLE = "user"
#logging func
def log(msg, level, trace = "NULL"):
    if trace == "NULL":
        print(datetime.datetime.now().strftime("%d/%m/%Y %H:%M:%S")+ " " + "[" + level + "]" + " " + msg)
    else:
        print(datetime.datetime.now().strftime("%d/%m/%Y %H:%M:%S")+ " " + "[" + level + "]" + " " + msg, trace)
        
        
class RequestResponseProcessor:

    def __init__(self, phoneNumber, params={}):
        dynamodb = boto3.resource('dynamodb')
        self.table = dynamodb.Table(USER_TABLE)
        self.phoneNumber = phoneNumber
        self.params = params
        self.schema = UserSchema()

    def update_user(self):
        try:
            query = self.table.get_item(Key={'phoneNumber':self.phoneNumber})
        except Exception as e:
            log(f"[{USER_TABLE}DB] Failed.", "ERROR", e)
            raise Exception(json.dumps({
                    "statusCode": HTTPStatus.INTERNAL_SERVER_ERROR.value,
                    "headers": {
                        "Content-Type": "application/json"
                    },
                    "message": f"Error interacting with DB {USER_TABLE}"
                })
            )
        else:
            print(query.get('Item'))
            if query.get('Item')==None:
                return False
            update_expression = ["set "]
            update_values = dict()
            print(self.params)
            for key, val in self.params.items():
                if(key == "phoneNumber"):
                    continue
                update_expression.append(f" {key} = :{key},")
                update_values[f":{key}"] = val
        
            update_expression="".join(update_expression)[:-1]
            response = self.table.update_item(
                Key={'phoneNumber':self.phoneNumber},
                UpdateExpression=update_expression,
                ExpressionAttributeValues=dict(update_values),
                ReturnValues="UPDATED_NEW"
                )
            return response['Attributes']

    def buildResponse(self):
        errors = self.schema.validate(self.request_body)
        if errors:
            return {
                'statusCode': HTTPStatus.BAD_REQUEST.value,
                "headers": {
                    "Content-Type": "application/json"
                },
                'body': json.dumps({
                    'message': errors
                })
            }

        payload = self.update_user()

        if payload:
            return {
                "statusCode": HTTPStatus.OK.value,
                "headers": {
                    "Content-Type": "application/json"
                },
                'body': self.schema.dumps(payload)
            }
        else:
            return {
                "statusCode": HTTPStatus.NOT_FOUND.value,
                "body": json.dumps({
                    "message": "Resource not found"
                })
            }

    def orchestrate(self):
        return self.buildResponse()
        

def lambda_handler(event, context):
    if(event.get("phoneNumber")==None):
        return {
                "statusCode": HTTPStatus.BAD_REQUEST.value,
                "headers": {
                    "Content-Type": "application/json"
                },
                "body": json.dumps({
                    "message": "phoneNumber key is not found"
                })
            }
    req = RequestResponseProcessor(event["phoneNumber"],event)
    res = req.orchestrate()

    log("[RESPONSE] " + str(res), "INFO")
    return res