from marshmallow import Schema, fields, INCLUDE
import base64


class CertSchema(Schema):

    phoneNumber = fields.String(required=True)
    fileExtension = fields.String(required=True)
    fileData = fields.String(required=True)
    


    class Meta:
        unknown = INCLUDE
