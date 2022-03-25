import Card from "components/Card/Card";
import React from "react";
import Chart from "react-apexcharts";
import { barChartData, barChartOptions } from "variables/charts";

export default function BarChart(props){

  return(
    <Card
      py="1rem"
      height={{ sm: "200px" }}
      width="100%"
      bg="linear-gradient(81.62deg, #313860 2.25%, #151928 79.87%)"
      position="relative"
    >
        <Chart
          options={props.barChartOptions}
          series={props.data}
          type="bar"
          width="100%"
          height="100%"
        />
    </Card>
  )
}


