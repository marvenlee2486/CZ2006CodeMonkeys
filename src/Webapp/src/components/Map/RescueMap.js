import React from 'react';
import GoogleMapReact from 'google-map-react';
import { Box, Popover, PopoverArrow, PopoverBody, PopoverCloseButton, PopoverContent, PopoverHeader, PopoverTrigger, Text } from '@chakra-ui/react';
import { HiLocationMarker, HiHeart } from "react-icons/hi";

function RescueMarker ({ timeStarted , phoneNumber, acceptedVolunteers,color }){
    console.log("res wtf");
    return(
        <Popover>
        <PopoverTrigger>
            {/* <Box><HiHeart style={{color: "red", fontSize:"2em"}}/></Box> */}
            <Box><HiLocationMarker style={{color: color, fontSize:"2em"}}/></Box>
        </PopoverTrigger>
        <PopoverContent>
            <PopoverArrow />
            <PopoverCloseButton />
            <PopoverHeader>
                Volunteers Accepted: {acceptedVolunteers.length}{"\n"}
            </PopoverHeader>

            <PopoverBody>
                <Text>Rescue started at: {timeStarted}</Text>
                <Text>Phone number of patient: {phoneNumber}</Text>
            </PopoverBody>
        </PopoverContent>
        </Popover>
    )
}

function VolunteerMarker ({ name , color }){
    console.log("vol wtf");
    return(
        <Popover>
        <PopoverTrigger>
            <Box><HiHeart style={{color: color, fontSize:"2em"}}/></Box>
        </PopoverTrigger>
        <PopoverContent>
            <PopoverArrow />
            <PopoverCloseButton />
            <PopoverHeader>
                Volunteer Name: {name}
            </PopoverHeader>
        </PopoverContent>
        </Popover>
    )
}

export default function RescueMap({markers}) {
    let volmarker = []
    let colors = []
    markers.forEach(marker=>{
        var color = Math.floor(Math.random()*16777215).toString(16);
        marker.accepted.forEach(vol=>volmarker.push(vol));
        colors.push('#'+color);
    });
    console.log(volmarker)
    const defaultProps = {
        center: {
        lat: 1.3542343864869617,
        lng: 103.81999172489604
        },
        zoom: 12
    };

    return (
        // Important! Always set the container height explicitly
        <div style={{ height: '100vh', width: '100%' }}>
        <GoogleMapReact
            bootstrapURLKeys={{ key: "AIzaSyDfrzVViXVF9PzqBp_QBoDOKhiY3uWS7os" }}
            defaultCenter={defaultProps.center}
            defaultZoom={defaultProps.zoom}
        >
            {markers.map((data,index)=>
                <RescueMarker
                key={index}
                lat={data.patientLat}
                lng={data.patientLon}
                timeStarted = {data.startTime}
                acceptedVolunteers = {data.accepted.length}
                phoneNumber = {data.patientTel}
                color = {colors[index]}
                />
            )}
            {volmarker.map((data,index)=>
                // console.log(index, data);
                <VolunteerMarker lat={data[1]} lng={data[2]} color={colors[index]}/>
            )}
        </GoogleMapReact>
        </div>
    );
}