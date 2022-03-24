import {
  Avatar,
  Badge,
  Button,
  Flex,
  Td,
  Text,
  Tr,
  useColorModeValue,
} from "@chakra-ui/react";
import React, { useState } from "react";
import { Auth } from 'aws-amplify';


function TablesTableRow(props) {
  const {phonenumber, name, age, status, date } = props;
  const textColor = useColorModeValue("gray.700", "white");
  const bgStatus = useColorModeValue("gray.400", "#1a202c");
  const colorStatus = useColorModeValue("white", "gray.400");
  const [data, setData] = useState();

  

  const deleteUser = async() => {
    console.log(phonenumber)
		const current_sessiontoken = await Auth.currentSession();
    const requestOptions = {
        method: 'DELETE',
        headers: {'Authorization': current_sessiontoken.idToken.jwtToken },
        body: JSON.stringify({ phoneNumber: phonenumber })
    };
    fetch('https://95emtg0gr2.execute-api.ap-southeast-1.amazonaws.com/staging/appusers', requestOptions)
        // .then(response => response.json())
        .then(data =>{
          // console.log(data)
          window.location.reload()
        });
  }

  return (
    <Tr>
      <Td>
        <Text fontSize="md" color={textColor} fontWeight="bold" pb=".5rem">
          {phonenumber}
        </Text>
      </Td>
      <Td minWidth={{ sm: "200px" }} pl="0px">
        <Flex align="center" py=".8rem" minWidth="100%" flexWrap="nowrap">
          <Flex direction="column">
            <Text
              fontSize="md"
              color={textColor}
              fontWeight="bold"
              minWidth="100%"
            >
              {name}
            </Text>
          </Flex>
        </Flex>
      </Td>
      <Td backgroundcolor='red'>
        <Text fontSize="md" color={textColor} fontWeight="bold" pb=".5rem">
          {age}
        </Text>
      </Td>
      <Td>
          <Text fontSize="md" color={textColor} fontWeight="bold" pb=".5rem">
            {date}
          </Text>
        </Td>
      <Td>
        <Badge
          // bg={status ? "green.400" : bgStatus}
          // color={status ? "white" : colorStatus}
          fontSize="16px"
          p="3px 10px"
          borderRadius="8px"
        >
          
          {status?"Volunteer":"Normal User"}
        </Badge>
      </Td>
      <Td>
        <Button p="0px" bg="transparent" variant="no-hover" onClick={()=>deleteUser()}>
          <Text
            fontSize="md"
            color="red.500"
            fontWeight="bold"
            cursor="pointer"
          >
            Remove
          </Text>
        </Button>
      </Td>
    </Tr>
  );
}

export default TablesTableRow;
