import { configureStore } from "@reduxjs/toolkit";
import {PullRequestReducer} from "./pull_request/PullRequestSlice"
import {DialogScreenReducer} from "./dialog/dialogSlice"
import customizationReducer from '../store/customizationReducer';
import { getDefaultMiddleware } from '@reduxjs/toolkit';

const reduxStore = configureStore({
  reducer: {
    data:PullRequestReducer,
    customization: customizationReducer,
    dialogScreen:DialogScreenReducer
  },
  middleware: getDefaultMiddleware =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),

});

export default reduxStore;