import { createSlice } from '@reduxjs/toolkit';

//서버랑 시간 맞는지 확인 필요( private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;              // 30분)
export const TOKEN_TIME_OUT = 30*60*1000;

export const tokenSlice = createSlice({
    name: 'authToken',
    initialState: {
        authenticated: false,
        accessToken: null,
        expireTime: null
    },
    reducers: {
        SET_TOKEN: (state, action) => {
            state.authenticated = true;
            state.accessToken = action.payload;
            state.expireTime = new Date().getTime() + TOKEN_TIME_OUT;
        },
        DELETE_TOKEN: (state) => {
            state.authenticated = false;
            state.accessToken = null;
            state.expireTime = null
        },
    }
})

export const { SET_TOKEN, DELETE_TOKEN } = tokenSlice.actions;

export default tokenSlice.reducer;