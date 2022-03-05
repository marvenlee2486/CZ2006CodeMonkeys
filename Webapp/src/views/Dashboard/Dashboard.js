// Chakra imports
import {
Box,
Button,
Flex,
Grid,
Icon,
Image,
Portal,
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
// assets
import googleImage from "assets/img/google_map.png";
import logoChakra from "assets/svg/logo-white.svg";
// Custom components
import Card from "components/Card/Card.js";
import CardBody from "components/Card/CardBody.js";
import CardHeader from "components/Card/CardHeader.js";
import BarChart from "components/Charts/BarChart";
import LineChart from "components/Charts/LineChart";
import IconBox from "components/Icons/IconBox";
// Custom icons
import {
CartIcon,
DocumentIcon,
GlobeIcon,
RocketIcon,
StatsIcon,
WalletIcon,
} from "components/Icons/Icons.js";
import DashboardTableRow from "components/Tables/DashboardTableRow";
import TimelineRow from "components/Tables/TimelineRow";
import React, { useState } from "react";
// react icons
import { BsArrowRight } from "react-icons/bs";
import { IoCheckmarkDoneCircleSharp } from "react-icons/io5";
import { dashboardTableData, timelineData } from "variables/general";

export default function Dashboard(props) {
	const { state } = props.location
	const requestOptions = {
        method: 'GET',
        headers: {
			'Content-Type': 'application/json',
			'Authorization': state.user.signInUserSession.idToken.jwtToken,
		 },
    };
    fetch('https://95emtg0gr2.execute-api.ap-southeast-1.amazonaws.com/staging', requestOptions)
	.then(response => {
		console.log(response)
		response.json()
	})
	.then(data => {
		console.log(data);
		alert(data);
	});

const value = "$100.000";
// Chakra Color Mode
const { colorMode, toggleColorMode } = useColorMode();
const iconTeal = useColorModeValue("teal.300", "teal.300");
const iconBoxInside = useColorModeValue("white", "white");
const textColor = useColorModeValue("gray.700", "white");
const [series, setSeries] = useState([
	{
	type: "area",
	name: "Mobile apps",
	data: [190, 220, 205, 350, 370, 450, 400, 360, 210, 250, 292, 150],
	},
	{
	type: "area",
	name: "Websites",
	data: [400, 291, 121, 117, 25, 133, 121, 211, 147, 25, 201, 203],
	},
]);
const overlayRef = React.useRef();

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
					18
				</StatNumber>
				</Flex>
			</Stat>
			<IconBox as="box" h={"45px"} w={"45px"} bg={iconTeal}>
				<DocumentIcon h={"24px"} w={"24px"} color={iconBoxInside} />
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
				Today's Volunteer
				</StatLabel>
				<Flex>
				<StatNumber fontSize="lg" color={textColor}>
					2,300
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
					+5%
				</StatHelpText>
				</Flex>
			</Stat>
			<IconBox as="box" h={"45px"} w={"45px"} bg={iconTeal}>
				<DocumentIcon h={"24px"} w={"24px"} color={iconBoxInside} />
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
				Pending Certificate
				</StatLabel>
				<Flex>
				<StatNumber fontSize="lg" color={textColor}>
					38
				</StatNumber>
				</Flex>
			</Stat>
			<Spacer />
			<IconBox as="box" h={"45px"} w={"45px"} bg={iconTeal}>
				<DocumentIcon h={"24px"} w={"24px"} color={iconBoxInside} />
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
				<Icon
				as={IoCheckmarkDoneCircleSharp}
				color="teal.300"
				w={4}
				h={4}
				pe="3px"
				/>
				<Text fontSize="sm" color="gray.400" fontWeight="normal">
				<Text fontWeight="bold" as="span">
					30
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
				<Th color="gray.400">Time</Th>
				<Th color="gray.400">User Name</Th>
				<Th color="gray.400">Responded Volunteer Number</Th>
			</Tr>
			</Thead>
			<Tbody>
			{dashboardTableData.map((row) => {
				return (
				<DashboardTableRow
					location={row.location}
					time={row.time}
					userName={row.userName}
					logo={row.logo}
					responedVolunteerNumber={row.responedVolunteerNumber}
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
				<Icon
				as={IoCheckmarkDoneCircleSharp}
				color="teal.300"
				w={4}
				h={4}
				pe="3px"
				/>
				<Text fontSize="sm" color="gray.400" fontWeight="normal">
				<Text fontWeight="bold" as="span">
					30
				</Text>{" "}
					in total.
				</Text>
			</Flex>
			</Flex>
		</CardHeader>
			<CardBody
				p="0px"
				backgroundImage={googleImage}
				bgPosition="center"
				bgRepeat="no-repeat"
				w="100%"
				h={{ sm: "200px", lg: "100%" }}
				bgSize="contain"
				position="relative"
				borderRadius="15px"
			>
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
			<BarChart />

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
				Recuse Events
				</Text>
				<Text fontSize="md" fontWeight="medium" color="gray.400">
				<Text as="span" color="gray.500" fontWeight="bold">
					(+23%)
				</Text>{" "}
				than last month
				</Text>
			</Flex>

			<SimpleGrid gap={{ sm: "12px" }} columns={3}>
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
					Succcessful
					</Text>
				</Flex>
				<Text
					fontSize="lg"
					color={textColor}
					fontWeight="bold"
					mb="6px"
					my="6px"
				>
					153
				</Text>
				<Progress
					colorScheme="teal"
					borderRadius="12px"
					h="5px"
					value={80}
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
					Fail
					</Text>
				</Flex>
				<Text
					fontSize="lg"
					color={textColor}
					fontWeight="bold"
					mb="6px"
					my="6px"
				>
					20
				</Text>
				<Progress
					colorScheme="teal"
					borderRadius="12px"
					h="5px"
					value={10}
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
					No Respond
					</Text>
				</Flex>
				<Text
					fontSize="lg"
					color={textColor}
					fontWeight="bold"
					mb="6px"
					my="6px"
				>
					10
				</Text>
				<Progress
					colorScheme="teal"
					borderRadius="12px"
					h="5px"
					value={10}
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
			<LineChart />
		</Box>
		</Card>

	</Grid>

	</Flex>
);
}
