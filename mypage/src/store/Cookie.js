import { Cookies } from 'react-cookie';


const cookies = new Cookies();

export const setAccessToken = (accessToken) => {
    const today = new Date();
    //refreshToken과 동일하게 7일로 설정(reissue를 하기 위함)
    const expireDate = today.setDate(today.getDate() + 7);

    return cookies.set('access_token', accessToken, { 
        sameSite: 'none', 
        path: "/", 
        secure : true,
        expires: new Date(expireDate),
        // httpOnly : true,
    });
};

export const getAccessToken = () => {
    return cookies.get('access_token');
};

export const removeAccessToken = () => {
    return cookies.remove('access_token', { sameSite: 'none', path: "/", secure : true })
}