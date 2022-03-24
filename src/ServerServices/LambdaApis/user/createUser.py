import boto3
import datetime
import json
from http import HTTPStatus
from shared.validation_schema import UserSchema
import os

USER_TABLE = "user"
#logging func
def log(msg):
    print(msg)

class RequestResponseProcessor:

    def __init__(self, request_body):
        dynamodb = boto3.resource('dynamodb')
        self.table = dynamodb.Table(USER_TABLE)
        self.request_body = request_body
        self.schema = UserSchema()

    def create_user(self):
        #self.request_body['website'] = self.schema.encode_url(self.request_body['website'])
        try:
            self.table.put_item(
                Item=self.schema.load(self.request_body)
            )
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
            return self.schema.load(self.request_body)

    def build_response(self):
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
        payload = self.create_user()
        return {
            'statusCode': HTTPStatus.CREATED.value,
            "headers": {
                "Content-Type": "application/json"
            },
            'body': self.schema.dumps(payload)
        }

    def orchestrate(self):
        return self.build_response()


def lambda_handler(event, context):
    log(str(event))
    req = RequestResponseProcessor(event)
    res = req.orchestrate()
    log("[RESPONSE] " + str(res))
    return res