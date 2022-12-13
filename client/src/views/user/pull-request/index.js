import React, { useEffect, useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import InfoIcon from "@mui/icons-material/Info";
import Stack from "@mui/material/Stack";
import FullScreenDialog from "ui-component/user/FullScreenDialog";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import LoadingCircle from "ui-component/user/LoadingCircle";
import Chip from '@mui/material/Chip';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import ButtonGroup from '@mui/material/ButtonGroup';
import ArrowDropDownIcon from '@mui/icons-material/ArrowDropDown';
import ClickAwayListener from '@mui/material/ClickAwayListener';
import Grow from '@mui/material/Grow';
import Paper from '@mui/material/Paper';
import Popper from '@mui/material/Popper';
import MenuItem from '@mui/material/MenuItem';
import MenuList from '@mui/material/MenuList';
import Box from "@mui/material/Box";
import axios from "axios";
import ConfirmDialog from "ui-component/user/ConfirmDialog";
import {getLastPage} from "../../../redux/pull_request/PullRequestSlice"

// Helper functions
function createData(
  pr,
  id,
  author,
  emailAddress,
  slug,
  create,
  updated,
  commentCount,
  title
) {
  return { pr, id, author, emailAddress, slug, create, updated, commentCount, title };
}

const options = ['OPEN', 'MERGED', 'DECLINED'];


// ==============================|| Pull-Request PAGE ||============================== //

const PullRequestPage = () => {
  const dispatch = useDispatch();
  const pullRequest = useSelector(state => state.data.pullRequest);
  const [pageSize, setPageSize] = useState(10);
  const [pageNum, setPageNum] = useState(0);
  const [open, setOpen] = useState(false);
  const [selectedPR, setSelectedPR] = useState({});
  const [buttonsOpen, setButtonsOpen] = React.useState(false);
  const anchorRef = React.useRef(null);
  const [selectedIndex, setSelectedIndex] = React.useState(0);
  const [buttons, setButtons] = useState("OPEN")
  const openPR = useSelector(state => state.data.openPR);
  const mergedPR = useSelector(state => state.data.mergedPR);
  const declinedPR = useSelector(state => state.data.declinedPR)
  const [data, setData] = useState(openPR);

  useEffect(() => {
    dispatch(getLastPage("pull-requests")) 
  }, [])

  if(pullRequest.length <= 0 || !pullRequest){
    return (
      <ConfirmDialog/>
    ); 
  }

  const handleClick = () => {
    console.info(`You clicked ${options[selectedIndex]}`);
  };

  const handleMenuItemClick = (event, index) => {
    setSelectedIndex(index);
    setButtonsOpen(false);
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

  const columns = [
    {
      field: "info",
      headerName: "INFO",
      filterable: false,
      disableClickEventBubbling: true,
      sortable: false,

      renderCell: (params) => {
        const onClick = (e) => {
          setSelectedPR({ ...params.row.pr });
          setOpen(true);
        };

        return (
          <Stack direction="row" spacing={2}>
            <IconButton onClick={onClick} sx={{ borderRadius: 25, color: "#21a2f6" }} aria-label="info">
  <InfoIcon />
</IconButton>
          </Stack>
        );
      },
      flex: 0.25,
    },
    {
      field: "author",
      headerName: "Author",
      flex: 0.5,
    },
    {
      field: "emailAddress",
      headerName: "E-mail",
      flex: 0.75,
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
      field: "commentCount",
      headerName: "Comments",
      flex: 0.5,
    },

    {
      field: "title",
      headerName: "Title",
      flex: 1.5,
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
      pr.values.properties.commentCount < 0 ? 0 : pr.values.properties.commentCount,
      pr.values.title
    );
  });

  if (data.length <= 0) {
    return (
      <LoadingCircle/>
    );
  }

  if (open) {
    return (
      <FullScreenDialog
        data={{ open: true, setOpen: setOpen, selectedPR: selectedPR }}
      />
    );
  }
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
                          setData([])
                          if(option == "MERGED" && buttons != "MERGED"){
                            setData(mergedPR)
                          }
                          else if(option == "DECLINED" && buttons != "DECLINED"){
                            setData(declinedPR)

                          }
                          else if(option == "OPEN" && buttons != "OPEN"){
                            setData(openPR)

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
        sx={{backgroundColor:"white", borderRadius:"20px", 
        border:"0px solid black !important",
          "& .MuiDataGrid-virtualScroller::-webkit-scrollbar": {             
              display: "none"
          
          },
      }}
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
