from marshmallow import Schema, fields, INCLUDE
import base64


class UserSchema(Schema):

    phoneNumber = fields.String(required=True)
    achievementsName = fields.List(fields.String())
    workAddress = fields.String()
    certificateUrl = fields.String()
    emergencyContactName = fields.String()
    medicalConditions = fields.List(fields.String())
    isVolunteer = fields.Boolean()
    name = fields.String()
    age = fields.Integer()

    class Meta:
        unknown = INCLUDE
