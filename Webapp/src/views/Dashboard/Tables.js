import React, { useEffect, useState } from "react";
// Chakra imports
import {
  Flex,
  Table,
  Tbody,
  Text,
  Th,
  Thead,
  Tr,
  useColorModeValue,
} from "@chakra-ui/react";
// Custom components
import Card from "components/Card/Card.js";
import CardHeader from "components/Card/CardHeader.js";
import CardBody from "components/Card/CardBody.js";
import TablesProjectRow from "components/Tables/TablesProjectRow";
import TablesTableRow from "components/Tables/TablesTableRow";
import { certificatesTableData, tablesTableData } from "variables/general";
import { useSelector } from 'react-redux'
import { Auth } from "aws-amplify";


function Tables() {
  const textColor = useColorModeValue("gray.700", "white");
  const userdata = useSelector((state) => state.userdata.data)
  const [usersData, setusersData] = useState([]);
  
  useEffect(()=>{
    getinfo();
  },[])


  const getinfo = async() => {
		//fetch data
		const current_sessiontoken = await Auth.currentSession();
		// console.log(current_sessiontoken.idToken.jwtToken);
		var res = await fetch(
				'https://95emtg0gr2.execute-api.ap-southeast-1.amazonaws.com/staging/appusers',
				{
					headers: {'Authorization': current_sessiontoken.idToken.jwtToken },
				},
			)
		var res = await res.json()
		console.log(res)
    setusersData(res)
  }


  return (
    <Flex direction="column" pt={{ base: "120px", md: "75px" }}>
      <Card overflowX={{ sm: "scroll", xl: "hidden" }}>
        <CardHeader p="6px 0px 22px 0px">
          <Text fontSize="xl" color={textColor} fontWeight="bold">
            Users Table
          </Text>
        </CardHeader>
        <CardBody>
          <Table variant="simple" color={textColor}>
            <Thead>
              <Tr my=".8rem" pl="0px" color="gray.400">
                <Th pl="0px" color="gray.400">Phone Number</Th>
                <Th pl="0px" color="gray.400">Name</Th>
                <Th pl="0px" color="gray.400">Age</Th>
                <Th color="gray.400">Date Joined</Th>
                <Th color="gray.400">Volunteer</Th>
                <Th></Th>
              </Tr>
            </Thead>
            <Tbody>
              {usersData.map((row) => {
                return (
                  <TablesTableRow
                    phonenumber={row.phoneNumber}
                    name={row.name}
                    age={row.age}
                    status={row.volunteer}
                    date={row.date_joined}
                  />
                );
              })}
            </Tbody>
          </Table>
        </CardBody>
      </Card>
      <Card
        my="22px"
        overflowX={{ sm: "scroll", xl: "hidden" }}
      >
        <CardHeader p="6px 0px 22px 0px">
          <Flex direction="column">
            <Text fontSize="lg" color={textColor} fontWeight="bold" pb=".5rem">
              Pending Certificates Table
            </Text>
          </Flex>
        </CardHeader>
        <CardBody>
          <Table variant="simple" color={textColor}>
            <Thead>
              <Tr my=".8rem" pl="0px">
                <Th pl="0px" color="gray.400">
                  Username
                </Th>
                <Th color="gray.400">Upload Date</Th>
                <Th></Th>
              </Tr>
            </Thead>
            <Tbody>
              {certificatesTableData.map((row) => {
                return (
                  <TablesProjectRow
                    name={row.username}
                    logo={row.logo}
                    // status={row.status}
                    // budget={row.budget}
                    date={row.date}
                    // progression={row.progression}
                  />
                );
              })}
            </Tbody>
          </Table>
        </CardBody>
      </Card>
    </Flex>
  );
}

export default Tables;
