import boto3
import base64
import json
import io
import os


bucket_name = "volunteercerticatebucket"

def lambda_handler(event,context):
    phone_number = event['phone_number']

    #file = io.BytesIO(bytes(event['file_data'], encoding='utf-8'))
    s3 = boto3.client('s3')

    image = s3.get_object(
        Bucket = bucket_name, 
        key = phone_number +'.png'
    )["Body"].read()
    print(image)

    return {
        'statusCode': 200,
        'headers': { "Content-Type":"image/png"},
        'body': base64.b64encode(image),
        'isBase64Encoded': True    
    }