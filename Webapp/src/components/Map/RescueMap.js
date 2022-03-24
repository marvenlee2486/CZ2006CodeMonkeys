import React from 'react';
import GoogleMapReact from 'google-map-react';
import { Box, Button, Popover, PopoverArrow, PopoverBody, PopoverCloseButton, PopoverContent, PopoverHeader, PopoverTrigger, Text } from '@chakra-ui/react';
import { HiLocationMarker } from "react-icons/hi";

function RescueMarker ({ timeStarted , victim, volunteercount }){
    return(
        <Popover>
        <PopoverTrigger>
            <Box><HiLocationMarker style={{color: "red", fontSize:"2em"}}/></Box>
        </PopoverTrigger>
        <PopoverContent>
            <PopoverArrow />
            <PopoverCloseButton />
            <PopoverHeader>Volunteers Responded: {volunteercount}</PopoverHeader>
            <PopoverBody>
                <Text>Rescue started at: {timeStarted}</Text>
                <Text>Victim number: {victim}</Text>
            </PopoverBody>
        </PopoverContent>
        </Popover>
    )
}

export default function RescueMap({markers}) {
    // console.log(typeof markers)
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
                lat={data.latitude}
                lng={data.longitude}
                timeStarted = {data.timeStarted}
                victim = {data.victim}
                volunteercount = {data.respondedVolunteers}
                />
            )}
           
        </GoogleMapReact>
        </div>
    );
}