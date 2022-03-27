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
  const { logo, name, date, progression, s3url } = props;
  const textColor = useColorModeValue("gray.700", "white");
  const { isOpen, onOpen, onClose } = useDisclosure();
  const [certURL, setCertURL] = useState("https://volunteercerticatebucket.s3.ap-southeast-1.amazonaws.com/cassie_yung_min.jpg");
  
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
        <Button p="0px" bg="transparent" onClick={onOpen}>
        <Text
            fontSize="md"
            color="blue.500"
            fontWeight="bold"
            cursor="pointer"
          >
            View
          </Text>
        </Button>
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
            <Button colorScheme='blue' mr={3}  onClick={() => alert("approved")}>
              Approve
            </Button>
            <Button colorScheme='red' onClick={() => alert("Rejected")}>Reject</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </Tr>
    
  );
}

export default DashboardTableRow;
