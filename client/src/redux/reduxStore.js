import { configureStore } from "@reduxjs/toolkit";
import { applyMiddleware } from 'redux'

import {PullRequestReducer} from "./pull_request/PullRequestSlice"
import {DialogScreenReducer} from "./dialog/dialogSlice"
import customizationReducer from '../store/customizationReducer';

const reduxStore = configureStore({
  reducer: {
    data:PullRequestReducer,
    customization: customizationReducer,
    dialogScreen:DialogScreenReducer
  },
});

export default reduxStore;