// Chakra imports
import {
Box,
Button,
Flex,
Grid,
Icon,
Progress,
SimpleGrid,
Spacer,
Stat,
StatHelpText,
StatLabel,
StatNumber,
Table,
Tbody,
Text,
Th,
Thead,
Tr,
useColorMode,
useColorModeValue,
} from "@chakra-ui/react";
// Custom components
import Card from "components/Card/Card.js";
import CardBody from "components/Card/CardBody.js";
import CardHeader from "components/Card/CardHeader.js";
import BarChart from "components/Charts/BarChart";
import LineChart from "components/Charts/LineChart";
import IconBox from "components/Icons/IconBox";
import RescueMap from "components/Map/RescueMap";
// Custom icons
import {
DocumentIcon,
RocketIcon,
} from "components/Icons/Icons.js";
import DashboardTableRow from "components/Tables/DashboardTableRow";
import React, { useEffect, useState } from "react";
// react icons
import { IoCheckmarkDoneCircleSharp, IoRemoveCircleSharp} from "react-icons/io5";
import { BiBroadcast, BiUserPlus ,BiTask } from "react-icons/bi";
// other imports
import { useHistory } from "react-router-dom";
import Amplify, { Auth } from 'aws-amplify';
import awsconfig from '../../aws-exports';
Amplify.configure(awsconfig);
import { lineChartOptions } from "variables/charts";
import { barChartOptions } from "variables/charts";
import { SiSocketdotio } from "react-icons/si";
import { forEach } from "lodash";


export default function Dashboard() {
	//variables--------------------------------------------------------------
	const [locationdata, setLocationData] = useState({state:"disconnected",data:[]});

	// Chakra Color Mode
	const { colorMode, toggleColorMode } = useColorMode();
	const iconTeal = useColorModeValue("teal.300", "teal.300");
	const iconBoxInside = useColorModeValue("white", "white");
	const textColor = useColorModeValue("gray.700", "white");
	const history = useHistory();
	const [lineChartData, setLineChartData] = useState([
		{
		  name: "Normal Users",
		  data: [50, 40, 300, 220, 500, 250, 400, 230, 500],
		},
		{
		  name: "Volunteers",
		  data: [30, 90, 40, 140, 290, 290, 340, 230, 400],
		},
	])
	const [lineChartOptionsnew, setLineChartOptions] = useState(lineChartOptions);
	const [barChartData , setBarChartData] = useState([{
		name: "Responded Volunteers",
		data: [2,3,10,20,20,30,40,40,45],
	}])
	const [barChartOptionsnew, setBarChartOptions] = useState(barChartOptions);
	const [totalRescues, setTotalRescues] = useState({"No Response":0,"Responded":0})
	const [pendingcerts, setPendingCerts] = useState(0);

	//Functions-----------------------------------------------------------------
	useEffect(() => {
		Auth.currentUserInfo().then(current_user => {if (current_user===null) history.push("/auth/signin");})
		setUsersInfo();
		setRescueInfo();
		getlocationdata();
    }, []);

	
	const getlocationdata = async () => {
		var ws = new WebSocket("wss://chfo8hmaua.execute-api.ap-southeast-1.amazonaws.com/production");
		
		ws.onopen = function () {
			console.log('connected to websocket');
			setLocationData(prevlocationdata => ({...prevlocationdata, state:"connected, retrieving info..."}));
			ws.send(JSON.stringify({"action":"setName","name":"admin"}))
		};

		ws.onmessage = async (evt) => {
			console.log(evt.data);
			// console.log(JSON.parse(evt.data)["privateMessage"]);
			let data = await JSON.parse(evt.data)
			if (data["privateMessage"]){
				console.log("yes private incoming")
				console.log(typeof data["privateMessage"]);
				data["privateMessage"].forEach(d=>console.log(d));
				setLocationData(prevlocationdata => ({state:"connected",data:data["privateMessage"]}));
			}
		};

		ws.onclose = function () {
			console.log('socket closed');
			setLocationData({"state":"disconnected","data":[]});
		};
	}


	const setUsersInfo = async() => {
		//fetch data
		const current_sessiontoken = await Auth.currentSession();
		var res = await fetch(
				'https://95emtg0gr2.execute-api.ap-southeast-1.amazonaws.com/staging/appusers',
				{
					headers: {'Authorization': current_sessiontoken.idToken.jwtToken },
				},
			)
		var res = await res.json()
		//Set pending certs
		setPendingCerts(res.filter(row=>row.isVolunteer && row.isVolunteer=="PENDING").length);

		//Set user monthly statistics
		let monthlyUserData = [0,0,0,0,0,0,0,0,0];
		let monthlyVolunteerData = [0,0,0,0,0,0,0,0,0];
		let months = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"]
		let monthsSet = []
		res.forEach(data=>{
			let dj = new Date(data.date_joined);
			let monthcheck = new Date()
			monthcheck.setDate(1);
			monthcheck.setMonth(monthcheck.getMonth() - 8);
			if (dj>=monthcheck){
				for (let i=0;i<9;i++){
					monthsSet.push(months[monthcheck.getMonth()]);
					if (dj<monthcheck.setMonth(monthcheck.getMonth() + 1)) {
						if (data.isVolunteer=="YES") monthlyVolunteerData[i]+=1;
						else monthlyUserData[i]+=1;
					}
				}
			}
		});
		setLineChartData([
			{name:"Normal Users",data:monthlyUserData},
			{name:"Volunteers",data:monthlyVolunteerData},
		]);
		setLineChartOptions({ ...lineChartOptionsnew, xaxis:{categories:monthsSet}});
	};

	const setRescueInfo = async() => {
		//fetch data
		const current_sessiontoken = await Auth.currentSession();
		console.log(current_sessiontoken)
		var res = await fetch(
				'https://95emtg0gr2.execute-api.ap-southeast-1.amazonaws.com/staging/rescueevents',
				{
					headers: {'Authorization': current_sessiontoken.accessToken.jwtToken },
				},
			)
		var res = await res.json()
		console.log(res)

		//Set rescue monthly statistics
		let monthlyRescueData = [0,0,0,0,0,0,0,0,0];
		let months = ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"]
		let monthsSet = []
		res.forEach(data=>{
			let date = data.dateTimeStarted.split("#")[0]
			let dj = new Date(date);
			// console.log(dj)
			let monthcheck = new Date()
			monthcheck.setDate(1);
			monthcheck.setMonth(monthcheck.getMonth() - 8);
			if (dj>=monthcheck){
				for (let i=0;i<9;i++){
					monthsSet.push(months[monthcheck.getMonth()]);
					if (dj<monthcheck.setMonth(monthcheck.getMonth() + 1)) {
						monthlyRescueData[i]+=1;
						if ( data.respondedVolunteers.length==0) setTotalRescues(prevTotals =>({...prevTotals,"No Response":prevTotals["No Response"]+1}));
						else setTotalRescues(prevTotals =>({...prevTotals,"Responded":prevTotals["Responded"]+1}));
						break;
					}
				}
			}
		});
		setBarChartData([
			{name:"Responded Volunteers",data:monthlyRescueData},
		]);
		setBarChartOptions({ ...barChartOptionsnew, xaxis:{categories:monthsSet}});
	}

	
	
	return (
		<Flex flexDirection="column" pt={{ base: "120px", md: "75px" }}>
			
		<SimpleGrid columns={{ sm: 1, md: 2, xl: 3 }} spacing="24px">

			<Card minH="83px">
			<CardBody>
				<Flex flexDirection="row" align="center" justify="center" w="100%">
				<Stat me="auto">
					<StatLabel
					fontSize="sm"
					color="gray.400"
					fontWeight="bold"
					pb=".1rem"
					>
					Ongoing Rescue Events
					</StatLabel>
					<Flex>
					<StatNumber fontSize="lg" color={textColor}>
						{locationdata.data.length}
					</StatNumber>
					</Flex>
				</Stat>
				<IconBox as="box" h={"45px"} w={"45px"} bg={iconTeal}>
					<BiBroadcast fontSize="2em"/>
				</IconBox>
				</Flex>
			</CardBody>
			</Card>

			<Card minH="83px">
			<CardBody>
				<Flex flexDirection="row" align="center" justify="center" w="100%">
				<Stat me="auto">
					<StatLabel
					fontSize="sm"
					color="gray.400"
					fontWeight="bold"
					pb=".1rem"
					>
					This month's users
					</StatLabel>
					<Flex>
					<StatNumber fontSize="lg" color={textColor}>
						{lineChartData[0].data.slice(-1)[0]}
					</StatNumber>
					<StatHelpText
						alignSelf="flex-end"
						justifySelf="flex-end"
						m="0px"
						color="green.400"
						fontWeight="bold"
						ps="3px"
						fontSize="md"
					>
						({lineChartData[0].data.slice(-2)[1]-lineChartData[0].data.slice(-2)[0]>=0?"+":"-"}
						{
							(lineChartData[0].data.slice(-2)[1]-lineChartData[0].data.slice(-2)[0])/lineChartData[0].data.slice(-2)[1]*100
						}%)
					</StatHelpText>
					</Flex>
				</Stat>
				<IconBox as="box" h={"45px"} w={"45px"} bg={iconTeal}>
					<BiUserPlus fontSize="2em"/>
				</IconBox>
				</Flex>
			</CardBody>
			</Card>

			<Card minH="83px">
			<CardBody>
				<Flex flexDirection="row" align="center" justify="center" w="100%">
				<Stat>
					<StatLabel
					fontSize="sm"
					color="gray.400"
					fontWeight="bold"
					pb=".1rem"
					>
					Pending Certificates
					</StatLabel>
					<Flex>
					<StatNumber fontSize="lg" color={textColor}>
						{pendingcerts}
					</StatNumber>
					</Flex>
				</Stat>
				<Spacer />
				<IconBox as="box" h={"45px"} w={"45px"} bg={iconTeal}>
					<BiTask fontSize="2em"/>
				</IconBox>
				</Flex>
			</CardBody>
			</Card>
		</SimpleGrid>

		{/* Ongoing Rescue Table */}
		<Grid
			templateColumns={{ md: "1fr", lg: "1fr" }}
			templateRows={{ md: "1fr auto", lg: "1fr" }}
			my="26px"
			gap="24px"
		>
			<Card p="16px" overflowX={{ sm: "scroll", xl: "hidden" }}>
			<CardHeader p="12px 0px 28px 0px">
				<Flex direction="column">
				<Text
					fontSize="lg"
					color={textColor}
					fontWeight="bold"
					pb=".5rem"
				>
					Ongoing Rescue Table
				</Text>
				<Flex align="center">
					{locationdata.data.length==0?
					<Icon
					as={IoRemoveCircleSharp}
					color="red.300"
					w={4}
					h={4}
					pe="3px"
					/>
					:
					<Icon
					as={IoCheckmarkDoneCircleSharp}
					color="teal.300"
					w={4}
					h={4}
					pe="3px"
					/>
					}
					<Text fontSize="sm" color="gray.400" fontWeight="normal">
					<Text fontWeight="bold" as="span">
						{locationdata.data.length}
					</Text>{" "}
						in total.
					</Text>
				</Flex>
				</Flex>
			</CardHeader>
			<Table variant="simple" color={textColor}>
				<Thead>
				<Tr my=".8rem" ps="0px">
					<Th ps="0px" color="gray.400">Location</Th>
					<Th ps="0px" color="gray.400">Time</Th>
					<Th ps="0px" color="gray.400">User Name</Th>
					<Th ps="0px" color="gray.400">Responded Volunteer Number</Th>
				</Tr>
				</Thead>
				<Tbody>
					{locationdata.data.map((row,index) => {
						return (
						!row.volunteer&&
						<DashboardTableRow
							key={index}
							lat={row.latitude}
							long={row.longitude}
							time={row.timeStarted}
							userName={row.victim}
							respondedVolunteerNumber={row.respondedVolunteers}
						/>
						);
					})}
				</Tbody>
			</Table>
			</Card>
		</Grid>

		{/* Ongoing Rescue Map */}
		<Grid
			templateColumns={{ md: "1fr", lg: "1fr" }}
			templateRows={{ md: "1fr auto", lg: "1fr" }}
			my="26px"
			gap="24px"
		>
			<Card minHeight="500px" p="1rem">
				<CardHeader p="12px 0px 28px 0px">
				<Flex direction="column">
				<Text
					fontSize="lg"
					color={textColor}
					fontWeight="bold"
					pb=".5rem"
				>
					Ongoing Rescue Map
				</Text>
				<Flex align="center">
					{locationdata.data.length==0?
					<Icon
					as={IoRemoveCircleSharp}
					color="red.300"
					w={4}
					h={4}
					pe="3px"
					/>
					:
					<Icon
					as={IoCheckmarkDoneCircleSharp}
					color="teal.300"
					w={4}
					h={4}
					pe="3px"
					/>
					}
					<Text fontSize="sm" color="gray.400" fontWeight="normal">
					<Text fontWeight="bold" as="span">
						{locationdata.data.length}
					</Text>{" "}
						in total.
					</Text>
				</Flex>
				</Flex>
			</CardHeader>
				<CardBody
					p="0px"
					bgPosition="center"
					bgRepeat="no-repeat"
					w="100%"
					h={{ sm: "200px", lg: "100%" }}
					bgSize="contain"
					position="relative"
					borderRadius="15px"
					justifyContent="center"
					alignItems="center"
				>
					<RescueMap markers={locationdata.data}/>
				</CardBody>
			</Card>
		</Grid>
		
		{/* History Statistics */}
		<Grid
			templateColumns={{ sm: "1fr", lg: "1.3fr 1.7fr" }}
			templateRows={{ sm: "repeat(2, 1fr)", lg: "1fr" }}
			gap="24px"
			my={{ lg: "26px" }}
		>
			<Card p="16px">
			<CardBody>
				<Flex direction="column" w="100%">
				<BarChart data={barChartData} barChartOptions={barChartOptionsnew}/>

				<Flex
					direction="column"
					mt="24px"
					mb="36px"
					alignSelf="flex-start"
				>
					<Text
					fontSize="lg"
					color={textColor}
					fontWeight="bold"
					mb="6px"
					>
					Rescue Events
					</Text>
					<Text fontSize="md" fontWeight="medium" color="gray.400">
					<Text as="span" color="gray.500" fontWeight="bold">
						({barChartData[0].data.slice(-1)[0]-barChartData[0].data.slice(-2,-1)[0]>0&&"+"}{
						((barChartData[0].data.slice(-1)[0]-barChartData[0].data.slice(-2,-1)[0])/barChartData[0].data.slice(-2,-1)[0])*100
						}%)
					</Text>{" "}
					than last month
					</Text>
				</Flex>

				<SimpleGrid gap={{ sm: "12px" }} columns={2}>
					<Flex direction="column">
					<Flex alignItems="center">
						<IconBox
						as="box"
						h={"30px"}
						w={"30px"}
						bg={iconTeal}
						me="6px"
						>
						<RocketIcon h={"15px"} w={"15px"} color={iconBoxInside} />
						</IconBox>
						<Text fontSize="sm" color="gray.400" fontWeight="semibold">
						Responded
						</Text>
					</Flex>
					<Text
						fontSize="lg"
						color={textColor}
						fontWeight="bold"
						mb="6px"
						my="6px"
					>
						{totalRescues["Responded"]}
					</Text>
					<Progress
						colorScheme="teal"
						borderRadius="12px"
						h="5px"
						value={totalRescues["Responded"]/(totalRescues["No Response"]+totalRescues["Responded"])*100}
					/>
					</Flex>
					<Flex direction="column">
					<Flex alignItems="center">
						<IconBox
						as="box"
						h={"30px"}
						w={"30px"}
						bg={iconTeal}
						me="6px"
						>
						<RocketIcon h={"15px"} w={"15px"} color={iconBoxInside} />
						</IconBox>
						<Text fontSize="sm" color="gray.400" fontWeight="semibold">
						No Response
						</Text>
					</Flex>
					<Text
						fontSize="lg"
						color={textColor}
						fontWeight="bold"
						mb="6px"
						my="6px"
					>
						{totalRescues["No Response"]}
					</Text>
					<Progress
						colorScheme="teal"
						borderRadius="12px"
						h="5px"
						value={totalRescues["No Response"]/(totalRescues["No Response"]+totalRescues["Responded"])*100}
					/>
					</Flex>
					
				</SimpleGrid>

				</Flex>
			</CardBody>
			</Card>

			<Card p="28px 10px 16px 0px" mb={{ sm: "26px", lg: "0px" }}>
			<CardHeader mb="20px" pl="22px">
				<Flex direction="column" alignSelf="flex-start">
				<Text fontSize="lg" color={textColor} fontWeight="bold" mb="6px">
					Users Overview
				</Text>
				<Text fontSize="md" fontWeight="medium" color="gray.400">
					In 2022
				</Text>
				</Flex>
			</CardHeader>
			<Box w="100%" h={{ sm: "300px" }} ps="8px">
				<LineChart data={lineChartData} lineChartOptions={lineChartOptionsnew}/>
			</Box>
			</Card>

		</Grid>

		</Flex>
	);
}
