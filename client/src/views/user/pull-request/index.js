import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import InfoIcon from "@mui/icons-material/Info";
import Stack from "@mui/material/Stack";
import FullScreenDialog from "ui-component/user/FullScreenDialog";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import { Vortex } from "react-loader-spinner";
import Divider from '@mui/material/Divider';
import Chip from '@mui/material/Chip';
import Button from '@mui/material/Button';
import ButtonGroup from '@mui/material/ButtonGroup';
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import ClickAwayListener from '@mui/material/ClickAwayListener';
import Grow from '@mui/material/Grow';
import Paper from '@mui/material/Paper';
import Popper from '@mui/material/Popper';
import MenuItem from '@mui/material/MenuItem';
import MenuList from '@mui/material/MenuList';
//import { useDemoData } from '@mui/x-data-grid-generator';

import Box from "@mui/material/Box";

import axios from "axios";

// Helper functions
function createData(
  pr,
  id,
  author,
  emailAddress,
  slug,
  create,
  updated,
  title
) {
  return { pr, id, author, emailAddress, slug, create, updated, title };
}

const options = ['OPEN', 'MERGED', 'DECLINED'];


// ==============================|| Pull-Request PAGE ||============================== //

const PullRequestPage = () => {
  const [data, setData] = useState([]);
  const [pageSize, setPageSize] = useState(10);
  const [pageNum, setPageNum] = useState(0);
  const [open, setOpen] = useState(false);
  const [selectedPR, setSelectedPR] = useState({});
  const [buttonsOpen, setButtonsOpen] = React.useState(false);
  const anchorRef = React.useRef(null);
  const [selectedIndex, setSelectedIndex] = React.useState(0);
  const [url, setUrl] = useState("http://localhost:8989/api/v1/get-data?query=values.open&condition=true")
  const [buttons, setButtons] = useState("OPEN")

  const handleClick = () => {
    console.info(`You clicked ${options[selectedIndex]}`);
  };

  const handleMenuItemClick = (event, index) => {
    setSelectedIndex(index);
    setButtonsOpen(false);
  };

  const handleChangeUrl = () => {
    setData([])
  };
  const handleToggle = () => {
    setButtonsOpen((prevOpen) => !prevOpen);
  };

  const handleClose = (event) => {
    if (anchorRef.current && anchorRef.current.contains(event.target)) {
      return;
    }

    setButtonsOpen(false);
  };

  useEffect(() => {
    const getData = async () => {
      const responseData = await axios(url);
      setData([...data, ...responseData.data]);
    };
    getData();
  }, [url]);

  const columns = [
    {
      field: "info",
      headerName: "INFO",
      filterable: false,
      disableClickEventBubbling: true,
      sortable: false,

      renderCell: (params) => {
        const onClick = (e) => {
          console.log(params);
          setSelectedPR({ ...params.row.pr });
          setOpen(true);
        };

        return (
          <Stack direction="row" spacing={2}>
            <Button
              sx={{ borderRadius: 25, color: "#21a2f6" }}
              variant="outlined"
              size="small"
              onClick={onClick}
            >
              <InfoIcon />
            </Button>
          </Stack>
        );
      },
      flex: 0.5,
    },
    {
      field: "author",
      headerName: "Author",
      flex: 0.5,
    },
    {
      field: "emailAddress",
      headerName: "E-mail",
      flex: 1,
    },
    {
      field: "slug",
      headerName: "Repository",
      flex: 0.5,
    },
    {
      field: "create",
      headerName: "Created",
      flex: 0.5,
    },
    {
      field: "updated",
      headerName: "Updated",
      flex: 0.5,
    },

    {
      field: "title",
      headerName: "Title",
      flex: 2,
    },
  ];

  const rows = data.map((pr) => {
    return createData(
      pr,
      pr.id,
      pr.values.author.user.name,
      pr.values.author.user.emailAddress,
      pr.values.fromRef.repository.slug,
      new Date(pr.values.createdDate).toISOString().split("T")[0],
      new Date(pr.values.updatedDate).toISOString().split("T")[0],
      pr.values.title
    );
  });

  if (data.length <= 0) {
    return (
      <Box
        sx={{
          width: "100%",
          height: "100%",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
        }}
      >
        <Vortex
          visible={true}
          height="250"
          width="250"
          ariaLabel="vortex-loading"
          wrapperStyle={{}}
          wrapperClass="vortex-wrapper"
          colors={["red", "green", "blue", "yellow", "orange", "purple"]} // 6 adet
        />
      </Box>
    );
  }

  if (open) {
    return (
      <FullScreenDialog
        data={{ open: true, setOpen: setOpen, selectedPR: selectedPR }}
      />
    );
  }
console.log("render sayısı")
  return (
    <>
    <Box>
    <ButtonGroup variant="contained" ref={anchorRef} aria-label="split button">
        <Button onClick={handleClick}>{options[selectedIndex]}</Button>
        <Button
          size="small"
          aria-controls={buttonsOpen ? 'split-button-menu' : undefined}
          aria-expanded={buttonsOpen ? 'true' : undefined}
          aria-label="select merge strategy"
          aria-haspopup="menu"
          onClick={handleToggle}
        >
          <ArrowDropDownIcon />
        </Button>
      </ButtonGroup>
      <Popper
        sx={{
          zIndex: 1,
        }}
        open={buttonsOpen}
        anchorEl={anchorRef.current}
        role={undefined}
        transition
        disablePortal
      >
        {({ TransitionProps, placement }) => (
          <Grow
            {...TransitionProps}
            style={{
              transformOrigin:
                placement === 'bottom' ? 'center top' : 'center bottom',
            }}
          >
            <Paper>
              <ClickAwayListener onClickAway={handleClose}>
                <MenuList id="split-button-menu" autoFocusItem>
                  {options.map((option, index) => (
                    <MenuItem
                      key={option}
                      selected={index === selectedIndex}
                      onClick={(event) => {
                        handleMenuItemClick(event, index)
                        if(option !== buttons){
                          setData(d => [])
                          if(option == "MERGED" && buttons != "MERGED"){
                            setUrl("http://localhost:8989/api/v1/get-data?query=values.state&condition=MERGED")

                          }
                          else if(option == "DECLINED" && buttons != "DECLINED"){
                            setUrl("http://localhost:8989/api/v1/get-data?query=values.state&condition=DECLINED")

                          }
                          else if(option == "OPEN" && buttons != "OPEN"){
                            setUrl("http://localhost:8989/api/v1/get-data?query=values.state&condition=OPEN")

                          }
                         else{
                          setData(d => console.log("gelen d ", d))
                         }
                         setButtons(option)

                        }
  
                      }
                        
                      }
                    >
                      {option}
                    </MenuItem>
                  ))}
                </MenuList>
              </ClickAwayListener>
            </Paper>
          </Grow>
        )}
      </Popper>
      </Box>
      <Box>
   
      <Box m="20px 0 0 0" height="75vh" sx={{backgroundColor:"white", borderRadius:"20px", border:"0px solid black !important"}}>
        <DataGrid
        sx={{backgroundColor:"white", borderRadius:"20px", border:"0px solid black !important"}}
          rows={rows}
          getRowId={rows.id}
          columns={columns}
          components={{ Toolbar: GridToolbar }}
          rowsPerPageOptions={[10, 25, 50, 75, 100]}
          pageSize={pageSize}
          onPageChange={(newPage) => setPageNum(newPage)}
          onPageSizeChange={(newPage) => setPageSize(newPage)} 
          initialState={{
            pagination: {
              page: pageNum,
            },
            filter: {
      filterModel: {
        items: [{ columnField: 'rating', operatorValue: '>', value: '2.5' }],
      },
    },
          }}
        ></DataGrid>
      </Box>
    </Box>
      </>
    
  );
};

export default PullRequestPage;
