import React, { useState } from "react";
import {
  Avatar,
  Tr,
  Td,
  Flex,
  Text,
  Progress,
  Icon,
  Button,
  useColorModeValue,
  useDisclosure,
  Modal,
  ModalOverlay,
  ModalHeader,
  ModalCloseButton,
  ModalBody,
  ModalFooter,
  ModalContent,
  Box,
} from "@chakra-ui/react";
import { FaEllipsisV } from "react-icons/fa";


function DashboardTableRow(props) {
  const {  name,s3url,phoneNumber } = props;
  const textColor = useColorModeValue("gray.700", "white");
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [status, setStatus] = useState("View");
  const [aloading, setaLoading] = useState(false);
  const [bloading, setbLoading] = useState(false);


  const approveCertificate = (decision) => {
    let isvolunteer;
    if (decision=="Approved"){
      setaLoading(true);
      isvolunteer="YES";
    }else{
      setbLoading(true);
      isvolunteer="REJECTED";
    }
    const requestOptions = {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ phoneNumber:phoneNumber,isVolunteer: isvolunteer })
    };
    fetch('https://w75577htk6.execute-api.ap-southeast-1.amazonaws.com/production/dbuser', requestOptions)
      .then(response => response.json())
      .then(data => {
        console.log(data);
        setStatus(decision);
        onClose();
        setaLoading(false);
        setbLoading(false)
      });
  }

  return (
    <Tr>
      <Td minWidth={{ sm: "250px" }} pl="0px">
        <Flex alignItems="center" py=".8rem" minWidth="100%" flexWrap="nowrap">
          <Text
            fontSize="md"
            color={textColor}
            fontWeight="bold"
            minWidth="100%"
          >
            {name}
          </Text>
        </Flex>
      </Td>
      {/* <Td>
        <Text fontSize="md" color={textColor} fontWeight="bold" pb=".5rem">
          {date}
        </Text>
      </Td> */}
      <Td>
        {status=="View"?
        <Button p="0px" bg="transparent" variant="no-hover" onClick={onOpen} >
        <Text
          fontSize="md"
          color="blue.500"
          fontWeight="bold"
        >
          {status}
        </Text>
        </Button>:
        <Text
          fontSize="md"
          color={status=="Approved"?"green.500":"red.500"}
          fontWeight="bold"
        >
          {status}
        </Text>
      }
      </Td>
      <Modal isOpen={isOpen} onClose={onClose} size='lg'>
        <ModalOverlay />
        <ModalContent h='600px' w='1500px'>
          <ModalHeader>{name}</ModalHeader>
          <ModalCloseButton />
          <ModalBody >
            <Flex alignItems='center' justifyContent='center'>
              <img src={s3url} alt="Cert Not Found"/>
            </Flex>
          </ModalBody>

          <ModalFooter>
            <Button isLoading={aloading} loadingText="Approving" colorScheme='blue' mr={3}  onClick={() => approveCertificate("Approved")}>
              Approve
            </Button>
            <Button isLoading={bloading} loadingText="Rejecting" colorScheme='red' onClick={() => approveCertificate("Rejected")}>Reject</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </Tr>
    
  );
}

export default DashboardTableRow;
