from marshmallow import Schema, fields, INCLUDE
import base64


class UserSchema(Schema):

    phoneNumber = fields.String(required=True)
    achievementsName = fields.List(fields.String())
    homeAddress = fields.String()
    homeLocation = fields.String()
    workAddress = fields.String()
    workLocation = fields.String()
    certificateUrl = fields.String()
    emergencyContactName = fields.String()
    emergencyContactNumber = fields.String()
    medicalConditions = fields.List(fields.String())
    isVolunteer = fields.String()
    numberofRescue = fields.Integer()
    name = fields.String()
    age = fields.Integer()


    class Meta:
        unknown = INCLUDE
