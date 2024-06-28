const newman = require('./newman_run.js'); 



const project_path="./src/OAPI_Pharmacy"
const collection = `${project_path}/Collection/AT EPhar.postman_collection.json`
// const environment = "./src/OAPI_Pharmacy/Environment/STAGING API PHARMACY.postman_environment.json"
const environment = `${project_path}/Environment/Mock Server.postman_environment.json`
const globals = `${project_path}/Globals/workspace.postman_globals.json`

// newman.newman_run(collection, environment, globals, "Add to Cart Endpoint", `${project_path}/Data/Order_Flow/Add to Cart Endpoint.csv`, 1,true,"cli");
newman.newman_run(collection, environment, globals, "Add to Cart Endpoint", `${project_path}/Data/Order_Flow/Add to Cart Endpoint.csv`, 1,log = "full");