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

    def __init__(self, phoneNumber=''):
        dynamodb = boto3.resource('dynamodb')
        self.table = dynamodb.Table(USER_TABLE)
        self.phoneNumber = phoneNumber
        self.schema = UserSchema()

    def get_user(self):
        try:
            query = self.table.get_item(Key={'phoneNumber':self.phoneNumber})
        except Exception as e:
            log(f"[{USER_TABLE}DB] Failed.", "ERROR", e)
            raise Exception(json.dumps({
                    "statusCode": HTTPStatus.INTERNAL_SERVER_ERROR.value,
                    "headers": {
                        "Content-Type": "application/json"
                    },
                    "message": f"Error interacting with DB {HOUSING_TABLE}"
                })
            )
        else:
            return query.get('Item')
        
    
    def build_response(self):
        errors = self.schema.validate({'phoneNumber': self.phoneNumber})
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

        payload = self.get_user()

        if payload:
            #payload['phoneNumber'] = self.schema.decode_url(payload['phoneNumber'])
            #print(payload)
            return {
                "statusCode": HTTPStatus.OK.value,
                "headers": {
                    "Content-Type": "application/json"
                },
                "body": self.schema.dumps(payload)
            }
        else:
            return {
                "statusCode": HTTPStatus.NOT_FOUND.value,
                "headers": {
                    "Content-Type": "application/json"
                },
                "body": json.dumps({
                    "message": "Resource not found"
                })
            }
        
    def orchestrate(self):
        return self.build_response()
        

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
    req = RequestResponseProcessor(event["phoneNumber"])
    res = req.orchestrate()
    log("[RESPONSE] " + str(res), "INFO")
    return res