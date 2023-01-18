export const loginUser = async (credentials) => {
  const option = {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json;charset=UTF-8'
      },
      body: JSON.stringify(credentials)
  };

  //api우리걸로 바꿔야함.
  const data = await getPromise('/user/login', option).catch(() => {
      return statusError;
  });

  if (parseInt(Number(data.status)/100)===2) {
      const status = data.ok;
      const code = data.status;
      const text = await data.text();
      const json = text.length ? JSON.parse(text) : "";

      return {
          status,
          code,
          json
      };
  } else {
      return statusError;
  }
};

export const logoutUser = async (credentials, accessToken) => {
  const option = {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json;charset=UTF-8'
      },
      body: JSON.stringify(credentials)
  };

  const data = await getPromise('/logout-url', option).catch(() => {
      return statusError;
  });

  if (parseInt(Number(data.status)/100)===2) {
      const status = data.ok;
      const code = data.status;
      const text = await data.text();
      const json = text.length ? JSON.parse(text) : "";

      return {
          status,
          code,
          json
      };
  } else {
      return statusError;
  }
}

export const requestToken = async (refreshToken) => {
  const option = {
      method: 'POST',
      headers: {
          'Content-Type': 'application/json;charset=UTF-8'
      },
      body: JSON.stringify({ refresh_token: refreshToken })
  }

  const data = await getPromise('/user/login', option).catch(() => {
      return statusError;
  });

  if (parseInt(Number(data.status)/100)===2) {
      const status = data.ok;
      const code = data.status;
      const text = await data.text();
      const json = text.length ? JSON.parse(text) : "";

      return {
          status,
          code,
          json
      };
  } else {
      return statusError;
  }
};