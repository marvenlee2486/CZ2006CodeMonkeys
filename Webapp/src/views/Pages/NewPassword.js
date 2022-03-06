import React from "react";
// Chakra imports
import {
  Box,
  Flex,
  Button,
  FormControl,
  FormLabel,
  Heading,
  Input,
  InputGroup,
  InputLeftAddon,
  InputRightElement,
  Link,
  Switch,
  Text,
  useColorModeValue,
} from "@chakra-ui/react";
// Assets
import signInImage from "assets/img/signInImage.png";
import ambulance from "assets/img/ambulance.png";
import { NavLink } from "react-router-dom";
import { useHistory } from "react-router-dom";
import Amplify, { Auth } from 'aws-amplify';
import awsconfig from '../../aws-exports';

Amplify.configure(awsconfig);
Auth.configure(awsconfig);
function NewPassword(props) {
  const history = useHistory();
  const { state } = props.location
  // Chakra color mode
  const titleColor = useColorModeValue("teal.300", "teal.200");
  const textColor = useColorModeValue("gray.400", "white");
  //useState variables
  const [show, setShow] = React.useState(false)
  const [newpassword, setNewPassword] = React.useState('')
  const [confirmpassword, setConfirmPassword] = React.useState('')
  //util functions
  const handleClick = () => setShow(!show)
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (newpassword!==confirmpassword) alert("Passwords don't match");
    else{
      if (state.user.challengeName === 'NEW_PASSWORD_REQUIRED') {
        Auth.completeNewPassword(
          state.user,               // the Cognito User Object
          newpassword,       // the new password
        ).then(user => {
          // at this time the user is logged in if no MFA required
          console.log(user);
          history.push({pathname:"/admin/dashboard",state:{user}});
        }).catch(e => {
          console.log(e);
        });
      } else {
          history.push('/auth/signin')// other situations
      }
    }
  };
  async function signIn() {
    try {
      const user = await Auth.signIn(username, password);
      console.log(user)
    } catch (error) {
        console.log('error signing in', error);
    }
  }

  return (
    <Flex position="relative" mb="40px">
      <Flex
        h={{ sm: "initial", md: "75vh", lg: "85vh" }}
        w="100%"
        maxW="1044px"
        mx="auto"
        justifyContent="space-between"
        mb="30px"
        pt={{ sm: "100px", md: "0px" }}
      >
        <Flex
          alignItems="center"
          justifyContent="start"
          style={{ userSelect: "none" }}
          w={{ base: "100%", md: "50%", lg: "42%" }}
        >
          <Flex
            direction="column"
            w="100%"
            background="transparent"
            p="48px"
            mt={{ md: "150px", lg: "80px" }}
          >
            <Heading color={titleColor} fontSize="32px" mb="10px">
              First time logging in?
            </Heading>
            <Text
              mb="36px"
              ms="4px"
              color={textColor}
              fontWeight="bold"
              fontSize="14px"
            >
              Enter your new password 
            </Text>
            <form onSubmit={handleSubmit}>
            <FormControl>
              <FormLabel ms="4px" fontSize="sm" fontWeight="normal">
                New Password
              </FormLabel>
              <InputGroup>
                <Input
                  borderRadius="15px"
                  mb="36px"
                  fontSize="sm"
                  type={show? 'text': "password"}
                  placeholder="Your new password"
                  size="lg"
                  onChange={event => setNewPassword(event.currentTarget.value)}
                />
                <InputRightElement width='4.5rem'>
                  <Button h='1.5rem' size='sm' onClick={handleClick}>
                    {show ? 'Hide' : 'Show'}
                  </Button>
                </InputRightElement>
              </InputGroup>
              <FormLabel ms="4px" fontSize="sm" fontWeight="normal">
                Confirm Password
              </FormLabel>
              <InputGroup>
                <Input
                  borderRadius="15px"
                  mb="36px"
                  fontSize="sm"
                  type={show? 'text': "password"}
                  placeholder="Confirm password"
                  size="lg"
                  onChange={event => setConfirmPassword(event.currentTarget.value)}
                />
                <InputRightElement width='4.5rem'>
                  <Button h='1.5rem' size='sm' onClick={handleClick}>
                    {show ? 'Hide' : 'Show'}
                  </Button>
                </InputRightElement>
              </InputGroup>
                <Button
                  fontSize="15px"
                  type="submit"
                  bg="teal.300"
                  w="100%"
                  h="45"
                  mb="20px"
                  color="white"
                  mt="20px"
                  _hover={{
                    bg: "teal.200",
                  }}
                  _active={{
                    bg: "teal.400",
                  }}
                >
                  SIGN IN
                </Button>
            </FormControl>
            </form>
          </Flex>
        </Flex>
        <Box
          display={{ base: "none", md: "block" }}
          overflowX="hidden"
          h="100%"
          w="40vw"
          position="absolute"
          right="0px"
        >
          <Box
            bgImage={ambulance}
            w="100%"
            h="100%"
            bgSize="cover"
            bgPosition="50%"
            position="absolute"
            borderBottomLeftRadius="20px"
          ></Box>
        </Box>
      </Flex>
    </Flex>
  );
}

export default NewPassword;
