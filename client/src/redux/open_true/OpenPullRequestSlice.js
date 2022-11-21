import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  openPullRequest:[],
};
const OpenPullRequestSlice = createSlice({
  name: "open", // this name using in action type
  initialState, //this is initial state
  reducers: {
    getOpenPullRequests: (state, action) => {
      console.log("GELEN DEĞER", action.payload)
      state.openPullRequest = action.payload;
      console.log("state openpr", state.openPullRequest)
    },
  },
});

export const OpenPullRequestReducer =  OpenPullRequestSlice.reducer;
export const { getOpenPullRequests } = OpenPullRequestSlice.actions;