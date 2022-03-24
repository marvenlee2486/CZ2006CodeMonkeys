from marshmallow import Schema, fields, INCLUDE
import base64


class rescueHistorySchema(Schema):

    datetime = fields.String(required=True)
    phoneNumber = fields.String(required=True)
    rescuersAccepted = fields.List(fields.String())
    rescueStatus = fields.String()

    class Meta:
        unknown = INCLUDE
