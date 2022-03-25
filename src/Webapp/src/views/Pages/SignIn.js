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
import { useHistory } from "react-router-dom";
import Amplify, { Auth } from 'aws-amplify';
import awsconfig from '../../aws-exports';
import { AiOutlineConsoleSql } from "react-icons/ai";

Amplify.configure(awsconfig);
// Auth.configure(awsconfig);
function SignIn() {
  // Chakra color mode
  const titleColor = useColorModeValue("teal.300", "teal.200");
  const textColor = useColorModeValue("gray.400", "white");
 const history = useHistory(); 
  //useState variables
  const [show, setShow] = React.useState(false)
  const [username, setUsername] = React.useState('')
  const [password, setPassword] = React.useState('')
  //util functions
  const handleClick = () => setShow(!show)
  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const user = await Auth.signIn(username, password);
      const current_authed = await Auth.currentUserInfo();
      if (current_authed!=null) history.push("/admin/dashboard");
      else if (user.challengeName === 'NEW_PASSWORD_REQUIRED') history.push({pathname:"/auth/newpassword",state:{user}})
    } catch (error) {
      alert(error.log);
    }
  };

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
          style={{ userSelect: "none" }}
          w={{ base: "100%", md: "50%", lg: "42%" }}
        >
          <Flex
            direction="column"
            w="100%"
            background="transparent"
            p="48px"
            mt={{ md: "150px", lg: "80px" }}
            justifyContent='center'
          >
            <Heading color={titleColor} fontSize="32px" mb="10px">
              Welcome Back
            </Heading>
            <Text
              mb="36px"
              ms="4px"
              color={textColor}
              fontWeight="bold"
              fontSize="14px"
            >
              Enter your username and password to sign in
            </Text>
            <form onSubmit={handleSubmit}>
            <FormControl>
              <FormLabel ms="4px" fontSize="sm" fontWeight="normal">
                Username
              </FormLabel>
              <InputGroup>
                <Input
                  borderRadius="15px"
                  mb="24px"
                  fontSize="sm"
                  type="text"
                  placeholder="Your username"
                  size="lg"
                  onChange={event => setUsername(event.currentTarget.value)}
                />
              </InputGroup>
              <FormLabel ms="4px" fontSize="sm" fontWeight="normal">
                Password
              </FormLabel>
              <InputGroup>
                <Input
                  borderRadius="15px"
                  mb="36px"
                  fontSize="sm"
                  type={show? 'text': "password"}
                  placeholder="Your password"
                  size="lg"
                  onChange={event => setPassword(event.currentTarget.value)}
                />
                <InputRightElement width='4.5rem' style={{marginTop:3.5}}>
                  <Button h='1.75rem' size='sm' onClick={handleClick}>
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

export default SignIn;
