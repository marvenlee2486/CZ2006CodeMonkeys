import boto3
import io
import os


bucket_name = "volunteercerticatebucket"

def lambda_handler(event,context):
    phone_number = event['phone_number']

    file = io.BytesIO(bytes(event['file_data'], encoding='utf-8'))
    s3 = boto3.resource('s3')
    bucket = s3.Bucket(bucket_name)

    bucket_object = bucket.Object(phone_number)
    bucket_object.upload_fileobj(file)

    return {
        'statusCode': 200,
        'body': f"Upload succeeded: {phone_number} has been uploaded to Amazon S3 in bucket {bucket_name}"
    }