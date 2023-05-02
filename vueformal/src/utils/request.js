import axios from 'axios'
import ElementUI from "element-ui";

const request = axios.create({
    timeout: 40000
})

// request interceptor
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user = localStorage.getItem("user")? JSON.parse(localStorage.getItem("user")) : null
    // add token
    if(user){
        config.headers['token'] = user.token;
    }

    return config
}, error => {
    return Promise.reject(error)
});

// response interceptor
request.interceptors.response.use(
    response => {
        let res = response.data;
        console.log(response.config.responseType)
        // if file is returned
        if (response.config.responseType === 'blob') {
            return res
        }
        // tolerate String
        if (typeof res === 'String') {
            res = res ? JSON.parse(res) : res
        }
        //when the token verification fail
        if(res.code === '401'){
            ElementUI.Message({
                message:res.msg,
                type:'error'
            })
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)


export default request

