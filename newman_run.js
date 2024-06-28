const newman = require('newman'); // require newman in your project
const fs = require('fs');

module.exports =
{
    newman_run
}



// const project_path="./src/OAPI_Pharmacy"
// const collection = `${project_path}/Collection/AT EPhar.postman_collection.json`
// // const environment = "./src/OAPI_Pharmacy/Environment/STAGING API PHARMACY.postman_environment.json"
// const environment = `${project_path}/Environment/Mock Server.postman_environment.json`
// const globals = `${project_path}/Globals/workspace.postman_globals.json`
// newman_run(collection, environment, globals, "Add to Cart Endpoint", `${project_path}/Data/Order_Flow/Add to Cart Endpoint.csv`, 1,true,"cli");

// call newman.run to pass `options` object and wait for callback
function newman_run(collection, environment, globals, folder, iterationData, iterationCount, log = "simple", reporter = "cli") {
    newman.run({
        collection: require(collection),
        environment: require(environment),
        globals: require(globals),
        iterationData: iterationData,
        iterationCount: iterationCount,
        folder: folder,
        reporters: reporter
    }).on('beforeRequest', (error, data) => {
        if (error) {
            console.log(error);
            return;
        }

        // if (data.request.body) {
        //     const requestName = data.item.name.replace(/[^a-z0-9]/gi, '-');
        //     const randomString = Math.random().toString(36).substring(7);
        //     const datetime = (new Date()).toISOString().replace(/[^0-9]/gi,'');
        //     const fileName = `./Log/${datetime}-${requestName}-${randomString}.json`;
        //     const content = data.request.body.raw;

        //     fs.writeFile(fileName, content, function (error) {
        //         if (error) { 
        //              console.error(error); 
        //         }
        //      });        
        // }
    })
        .on('request', (error, data) => {
            if (error) {
                console.log(error);
                return;
            }
            if (log) {
                let content = "No data";

                let req_body = {}
                if (data.request.body) {
                    req_body = JSON.parse(JSON.stringify(data.request.body));
                    req_body.raw = JSON.parse(req_body.raw)
                }
                let res_body = {}
                try { res_body = JSON.parse(data.response.stream.toString()) } catch (e) { res_body = data.response.stream.toString(); }

                if (log.toLowerCase() == "simple") {
                    content = {
                        iteration: data.cursor.iteration,
                        name: data.item.name,
                        method: data.request.method, //data.history.execution.data[0].request.method,
                        url: data.history.execution.data[0].request.href,
                        request: {
                            request_header: data.item.request.headers,
                            request_body: req_body
                        },
                        response: {
                            statusCode: data.response.code, //data.history.execution.data[0].response.statusCode,
                            status: data.response.status,
                            responseTime: data.response.responseTime,
                            responseSize: data.response.responseSize,
                            response_body: res_body,
                            response_header: data.history.execution.data[0].response.headers
                        }

                    }
                } else if (log.toLowerCase() == "full") {
                    content = {
                        iteration: data.cursor.iteration,
                        name: data.item.name,
                        method: data.request.method, //data.history.execution.data[0].request.method,
                        url: data.history.execution.data[0].request.href,
                        statusCode: data.response.code, //data.history.execution.data[0].response.statusCode,
                        status: data.response.status,
                        responseTime: data.response.responseTime,
                        responseSize: data.response.responseSize,
                        request: {
                            request_header: data.history.execution.data[0].request.headers,
                            request_body: req_body
                        },
                        response: {
                            response_header: data.history.execution.data[0].response.headers,
                            response_body: res_body
                        },
                        cursor: data.cursor
                    }
                } else if (log.toLowerCase() == "verbose") {
                    content = JSON.parse(JSON.stringify(data.request.body));
                }

                const requestName = data.item.name.replace(/[^a-z0-9]/gi, '-');
                const randomString = Math.random().toString(36).substring(7);
                const datetime = (new Date()).toISOString().replace(/[^0-9]/gi, '');
                const fileName = `./Log/${datetime}-${requestName}-${randomString}.json`;

                fs.writeFile(fileName, JSON.stringify(content, null, 4), function (error) {
                    if (error) {
                        console.error(error);
                    }
                });
            }
            /// TODO: all global error handling
        });
}