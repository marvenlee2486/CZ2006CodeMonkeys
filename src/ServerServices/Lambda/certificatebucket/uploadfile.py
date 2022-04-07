import boto3
import io
import os
import json
import base64
import requests
import datetime
from http import HTTPStatus
from shared.validation_schema import CertSchema
import os

import boto3
import io
import os
import json
import base64
import requests

bucket_name = "volunteercerticatebucket"

class RequestResponseProcessor:

    def __init__(self, request_body):
        s3 = boto3.resource('s3')
        self.request_body = request_body
        self.schema = CertSchema()
        self.url = ""
    def uploadcert(self):
        #self.request_body['website'] = self.schema.encode_url(self.request_body['website'])
        try:
            URL_EXPIRATION_SECONDS = 300
            Key = self.request_body["phoneNumber"] + "." + self.request_body["fileExtension"]
            # s3Params = {
            #     "Bucket": bucket_name,
            #     "Key" : Key,
            #     "Expires": URL_EXPIRATION_SECONDS,
            #     "ContentType": 'image/'+event["fileExtension"],
            # }
            s3_client = boto3.client('s3')
            #uploadURL = bucket.generate_presigned_url('putObject', s3Params)
            response = s3_client.generate_presigned_url('get_object', Params={'Bucket': bucket_name, 'Key': Key}, ExpiresIn=URL_EXPIRATION_SECONDS)
            print(response)
            return response
        except Exception as e:
            log(f"[{BUCKET_NAME}DB] Failed.", "ERROR", e)
            raise Exception(json.dumps({
                    "statusCode": HTTPStatus.INTERNAL_SERVER_ERROR.value,
                    "headers": {
                        "Content-Type": "application/json"
                    },
                    "message": f"Error interacting with S3 Bucket {BUCKET_NAME}"
                })
            )
       

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
        payload = self.uploadcert()
        return {
            'statusCode': HTTPStatus.CREATED.value,
            "headers": {
                "Content-Type": "application/json"
            },
            'body': payload
        }

    def orchestrate(self):
        return self.build_response()


def lambda_handler(event,context):
    print(str(event))
    try:
        event = event["body"]
    except:
        event = event
    print(str(event))
    req = RequestResponseProcessor(event)
    res = req.orchestrate()
    print(res)
    return json.loads(json.dumps(res))
    
    

