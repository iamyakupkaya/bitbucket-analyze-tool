
import Box from '@mui/material/Box';
import { useEffect, useState } from 'react';
import LoadingCircle from 'ui-component/user/LoadingCircle';
import axios from 'axios';
import { Chart } from "react-google-charts";
import { useSelector, useDispatch } from 'react-redux';



// ==============================|| SAMPLE PAGE ||============================== //


const HomePage = () => {
    const dispatch = useDispatch();
    const totalUsers = useSelector(state => state.data.allUser)
      const options = {
        title: `Total Author: ${totalUsers.length}`,
        is3D: true,
        backgroundColor: "#e3f2fd",
     
    
      };

      const chartData = [
        ["General İnfo",  "Total Counts" ],
        ["Active", 2],
        ["Inactive", 3],
      ];

 
    return (
    <>
        <Chart
            chartType="PieChart"
            data={chartData}
            options={options}
            width={"100%"}
            height={"400px"}
            
            />
    </>
       
)
};

export default HomePage;


/*


import SampleService from 'services/sample/SampleService';
import ServiceCaller from 'services/ServiceCaller';


const SamplePage = () => {
    const [data, setData] = useState([]);
    const [isSuccess, setSuccess] = useState(false);
    const getData = () => {
        let serviceCaller = new ServiceCaller();
        SampleService.getProducts(serviceCaller, '', (res) => {
            setSuccess(true);
            setData(res.products);
        }, (err) => {
            setSuccess(false);
            console.log(err);
        })
      }
    
      useEffect(() => {
        getData()
      }, []);    

    return(
        <Grid container spacing={gridSpacing}>
            <Grid item xs={12}>
                {data?.map((product) => (
                         <Typography variant="body2">
                            {product.title}
                        </Typography>
                    )
                )}:(
                    <Typography variant="body2">
                        No Product Data
                    </Typography>
                )
                
            
            </Grid>
        </Grid>
    )
};

*/