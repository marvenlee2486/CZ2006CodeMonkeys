from marshmallow import Schema, fields, INCLUDE
import base64


class rescueHistorySchema(Schema):
    
    phoneNumber = fields.String(required=True)
    dateTimeStarted = fields.String(required=True)
    respondedVolunteers = fields.List(fields.String())
    rescueStatus = fields.String()

    class Meta:
        unknown = INCLUDE
