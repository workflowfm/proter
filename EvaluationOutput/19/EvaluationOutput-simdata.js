var tasks = [
	"Calculate Rating",
	"Calculate Rating Manually",
	"Process Rating",
];

var resourceData = [
{label: "r1", times: [
	{"label":"Calculate Rating (Sim0)", task: "Calculate Rating", "id":"4a9c28eb-374a-42b5-8944-303dd4909319", "starting_time": 0, "ending_time": 5, delay: 0, cost: 0},
	{"label":"Process Rating (Sim0)", task: "Process Rating", "id":"d2d538e6-4e96-4226-a614-8c57b888c271", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim1)", task: "Calculate Rating", "id":"5c62957e-cae3-4412-89d5-ee424005b7aa", "starting_time": 40, "ending_time": 45, delay: 0, cost: 0},
	{"label":"Process Rating (Sim1)", task: "Process Rating", "id":"18f26aec-f1ba-49e8-b71a-cf02b2b4889e", "starting_time": 50, "ending_time": 55, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim2)", task: "Calculate Rating", "id":"ed785ffc-498e-474d-a210-4306baa9d1fd", "starting_time": 80, "ending_time": 85, delay: 0, cost: 0},
	{"label":"Process Rating (Sim2)", task: "Process Rating", "id":"bed74714-ea7e-41d9-983d-23cd64bbc525", "starting_time": 90, "ending_time": 95, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim3)", task: "Calculate Rating", "id":"fb54ac2f-c65a-4f70-8776-1ef3b45361ad", "starting_time": 120, "ending_time": 125, delay: 0, cost: 0},
	{"label":"Process Rating (Sim3)", task: "Process Rating", "id":"4f16e4be-2ef3-43c7-93bd-221aa56b2077", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim4)", task: "Calculate Rating", "id":"ad152378-aa49-4bc4-9159-6c9b9afe3258", "starting_time": 160, "ending_time": 165, delay: 0, cost: 0},
	{"label":"Process Rating (Sim4)", task: "Process Rating", "id":"b9cfa507-0cf3-4bfd-9cf2-643d7c7fc132", "starting_time": 170, "ending_time": 175, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim5)", task: "Calculate Rating", "id":"7a4bd310-889f-4aba-ae7b-200685e06213", "starting_time": 200, "ending_time": 205, delay: 0, cost: 0},
	{"label":"Process Rating (Sim5)", task: "Process Rating", "id":"ca5d5092-bfe7-4b3a-a610-eb8d46c5e70b", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim6)", task: "Calculate Rating", "id":"2fbccda1-53e7-4848-a4a9-5d9c762caf6e", "starting_time": 240, "ending_time": 245, delay: 0, cost: 0},
	{"label":"Process Rating (Sim6)", task: "Process Rating", "id":"00a9ff66-8138-4566-a1bd-1f9aba58ebb6", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim7)", task: "Calculate Rating", "id":"939e49b1-17c3-48a0-a10f-4c8cb68c50ef", "starting_time": 280, "ending_time": 285, delay: 0, cost: 0},
	{"label":"Process Rating (Sim7)", task: "Process Rating", "id":"e824765d-c56c-4874-b6fd-8533df089df7", "starting_time": 290, "ending_time": 295, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim8)", task: "Calculate Rating", "id":"a726636d-d854-4d2b-b324-fad03adf9ed1", "starting_time": 320, "ending_time": 325, delay: 0, cost: 0},
	{"label":"Process Rating (Sim8)", task: "Process Rating", "id":"880b8f88-5033-413a-aaef-e90243e69226", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim9)", task: "Calculate Rating", "id":"6c933218-cf4d-4ff9-8dcf-a798614171ab", "starting_time": 360, "ending_time": 365, delay: 0, cost: 0},
	{"label":"Process Rating (Sim9)", task: "Process Rating", "id":"c88ac256-8bab-4217-a12c-6b0da8eaff5a", "starting_time": 370, "ending_time": 375, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim10)", task: "Calculate Rating", "id":"636f7ed9-79bb-4169-9529-3ee6e6974969", "starting_time": 400, "ending_time": 405, delay: 0, cost: 0},
	{"label":"Process Rating (Sim10)", task: "Process Rating", "id":"ef12b8de-80bf-4646-abcc-825bbbf0f7e2", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim11)", task: "Calculate Rating", "id":"79324154-95cb-4318-8dea-f64ebc5ec3a3", "starting_time": 440, "ending_time": 445, delay: 0, cost: 0},
	{"label":"Process Rating (Sim11)", task: "Process Rating", "id":"13a1882e-2a8b-462c-b484-607ecb9a42ff", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim12)", task: "Calculate Rating", "id":"9fe557ac-8604-474a-a4d8-33eaa782368e", "starting_time": 480, "ending_time": 485, delay: 0, cost: 0},
	{"label":"Process Rating (Sim12)", task: "Process Rating", "id":"abf0bddb-d00c-4aed-b370-284b9f5f6e9b", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim13)", task: "Calculate Rating", "id":"c7c8de74-d7bf-48be-ba51-cac041ead318", "starting_time": 520, "ending_time": 525, delay: 0, cost: 0},
	{"label":"Process Rating (Sim13)", task: "Process Rating", "id":"6325335c-b457-4ddc-8288-44d6218a795b", "starting_time": 530, "ending_time": 535, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim14)", task: "Calculate Rating", "id":"dd2332be-dd5c-4532-a8ff-8a935cb954fe", "starting_time": 560, "ending_time": 565, delay: 0, cost: 0},
	{"label":"Process Rating (Sim14)", task: "Process Rating", "id":"d40cd54c-82f5-42e0-9f4a-2ad8fccb8b45", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim15)", task: "Calculate Rating", "id":"aad7187c-6356-4ad5-8e67-b467b3d89b70", "starting_time": 600, "ending_time": 605, delay: 0, cost: 0},
	{"label":"Process Rating (Sim15)", task: "Process Rating", "id":"b994499a-f863-4955-97c5-184decf8e64e", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim16)", task: "Calculate Rating", "id":"a9f3ca6b-e8f5-4f31-b44c-6a08fcc1f85c", "starting_time": 640, "ending_time": 645, delay: 0, cost: 0},
	{"label":"Process Rating (Sim16)", task: "Process Rating", "id":"458140ab-27bd-4df4-9538-8473850af7b0", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim17)", task: "Calculate Rating", "id":"9a9eec26-f80d-45d9-96b0-c935642b52a1", "starting_time": 680, "ending_time": 685, delay: 0, cost: 0},
	{"label":"Process Rating (Sim17)", task: "Process Rating", "id":"65bc4797-ed87-4380-8032-3415eb2c42bc", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim18)", task: "Calculate Rating", "id":"b5ef88fa-7b1a-4e05-b1d2-8f984cfa614d", "starting_time": 720, "ending_time": 725, delay: 0, cost: 0},
	{"label":"Process Rating (Sim18)", task: "Process Rating", "id":"753dca36-7422-4a5d-a605-7e5e5fc1158b", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim19)", task: "Calculate Rating", "id":"870455ee-8370-40dc-bece-445ef4d55b69", "starting_time": 760, "ending_time": 765, delay: 0, cost: 0},
	{"label":"Process Rating (Sim19)", task: "Process Rating", "id":"2d2d55f2-85eb-4c7c-8387-e0f8aa1c697e", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim20)", task: "Calculate Rating", "id":"94103fde-7159-434b-9dbf-21a4df832663", "starting_time": 800, "ending_time": 805, delay: 0, cost: 0},
	{"label":"Process Rating (Sim20)", task: "Process Rating", "id":"ebbd548d-d62a-4527-a7bc-b8baa859587e", "starting_time": 810, "ending_time": 815, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim21)", task: "Calculate Rating", "id":"d6dd50f0-cc18-4ab3-bb75-6606bf2144ad", "starting_time": 840, "ending_time": 845, delay: 0, cost: 0},
	{"label":"Process Rating (Sim21)", task: "Process Rating", "id":"e3d5dd19-5eca-4bc6-b128-b497afac47e4", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim22)", task: "Calculate Rating", "id":"dc3e646f-b0f5-4c36-8a08-2b92d89f2088", "starting_time": 880, "ending_time": 885, delay: 0, cost: 0},
	{"label":"Process Rating (Sim22)", task: "Process Rating", "id":"70dd5aa4-ffb6-42d6-9155-38abcfb1978d", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim23)", task: "Calculate Rating", "id":"7548ba63-1ee8-4065-bee9-7ce991cf6f01", "starting_time": 920, "ending_time": 925, delay: 0, cost: 0},
	{"label":"Process Rating (Sim23)", task: "Process Rating", "id":"1cc8de9a-388b-49f0-b50c-f0fb0ae2aade", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim24)", task: "Calculate Rating", "id":"e707c55e-48d7-413b-b78a-29600f70be4e", "starting_time": 960, "ending_time": 965, delay: 0, cost: 0},
	{"label":"Process Rating (Sim24)", task: "Process Rating", "id":"abaf1919-db99-4286-9248-1fb518de1db4", "starting_time": 970, "ending_time": 975, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim25)", task: "Calculate Rating", "id":"8d6348c0-2dbe-4e4f-95d7-8fe6d32be03d", "starting_time": 1000, "ending_time": 1005, delay: 0, cost: 0},
	{"label":"Process Rating (Sim25)", task: "Process Rating", "id":"75c1507f-df72-4ea9-bbb9-79e51e59be6a", "starting_time": 1010, "ending_time": 1015, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim26)", task: "Calculate Rating", "id":"7f2f056b-4a77-4241-a94b-f54f9227be64", "starting_time": 1040, "ending_time": 1045, delay: 0, cost: 0},
	{"label":"Process Rating (Sim26)", task: "Process Rating", "id":"01ee3717-e26f-4ddd-8e7a-127c87c8cf87", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim27)", task: "Calculate Rating", "id":"26cb41dc-bc5b-413a-ac89-9fea69d1745b", "starting_time": 1080, "ending_time": 1085, delay: 0, cost: 0},
	{"label":"Process Rating (Sim27)", task: "Process Rating", "id":"ab0fcad6-201e-451c-8bce-f12e654931dc", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim28)", task: "Calculate Rating", "id":"982ce78c-684c-4248-a876-abbf003171ae", "starting_time": 1120, "ending_time": 1125, delay: 0, cost: 0},
	{"label":"Process Rating (Sim28)", task: "Process Rating", "id":"b7623fa9-cc31-46a9-9281-cdd5b870f5ef", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim29)", task: "Calculate Rating", "id":"a68463ec-46aa-47c2-930b-ec7fd4fa8808", "starting_time": 1160, "ending_time": 1165, delay: 0, cost: 0},
	{"label":"Process Rating (Sim29)", task: "Process Rating", "id":"5c4471c0-3fb0-402c-a746-00925d5e06ea", "starting_time": 1170, "ending_time": 1175, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim30)", task: "Calculate Rating", "id":"b379a394-fb04-4564-8090-52628c09aee1", "starting_time": 1200, "ending_time": 1205, delay: 0, cost: 0},
	{"label":"Process Rating (Sim30)", task: "Process Rating", "id":"9b3d1179-12b2-4bed-8eb4-8734bb5f4f36", "starting_time": 1210, "ending_time": 1215, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim31)", task: "Calculate Rating", "id":"abe65871-e029-44c8-8ca4-adad8fb9ae36", "starting_time": 1240, "ending_time": 1245, delay: 0, cost: 0},
	{"label":"Process Rating (Sim31)", task: "Process Rating", "id":"b0b2bbf5-17bf-4ed6-86e3-efa322516aca", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim32)", task: "Calculate Rating", "id":"0f6c5452-4135-4d78-a966-5256746f5aa2", "starting_time": 1280, "ending_time": 1285, delay: 0, cost: 0},
	{"label":"Process Rating (Sim32)", task: "Process Rating", "id":"71d4da8b-940d-46e5-81fa-86cb1caf0614", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim33)", task: "Calculate Rating", "id":"c515b963-45b4-416d-abd0-251fb5978f13", "starting_time": 1320, "ending_time": 1325, delay: 0, cost: 0},
	{"label":"Process Rating (Sim33)", task: "Process Rating", "id":"08789fc3-a7b9-407b-b76f-fc2367ed132b", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim34)", task: "Calculate Rating", "id":"a86631ad-4b33-436d-ae6f-dfebd92e28d3", "starting_time": 1360, "ending_time": 1365, delay: 0, cost: 0},
	{"label":"Process Rating (Sim34)", task: "Process Rating", "id":"88dd87db-26f7-4efe-9f82-588a80339e5c", "starting_time": 1370, "ending_time": 1375, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim35)", task: "Calculate Rating", "id":"8b6d84a8-f668-4d28-b14a-c0b566c2b336", "starting_time": 1400, "ending_time": 1405, delay: 0, cost: 0},
	{"label":"Process Rating (Sim35)", task: "Process Rating", "id":"44e3e603-4eaf-44b4-b790-fe7e063ec8d5", "starting_time": 1410, "ending_time": 1415, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim36)", task: "Calculate Rating", "id":"8bb75cbf-2e08-42cb-b282-044bcb5b9a01", "starting_time": 1440, "ending_time": 1445, delay: 0, cost: 0},
	{"label":"Process Rating (Sim36)", task: "Process Rating", "id":"fef4e5f9-580c-43f3-a3f9-fc6ab3c61275", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim37)", task: "Calculate Rating", "id":"be05cbb1-e4e3-4596-a741-b4616264b174", "starting_time": 1480, "ending_time": 1485, delay: 0, cost: 0},
	{"label":"Process Rating (Sim37)", task: "Process Rating", "id":"80481a7e-816a-4564-916b-00120eda17c1", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim38)", task: "Calculate Rating", "id":"0df99a97-24e6-4dca-a9e8-0f777121f327", "starting_time": 1520, "ending_time": 1525, delay: 0, cost: 0},
	{"label":"Process Rating (Sim38)", task: "Process Rating", "id":"027e69ba-5258-4558-ad37-37247d8747d4", "starting_time": 1530, "ending_time": 1535, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim39)", task: "Calculate Rating", "id":"80749d61-958b-4556-b5c7-9c1b0e54bb8b", "starting_time": 1560, "ending_time": 1565, delay: 0, cost: 0},
	{"label":"Process Rating (Sim39)", task: "Process Rating", "id":"d9b30875-8fb1-46d9-83cc-ce28e13bab71", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim40)", task: "Calculate Rating", "id":"20c96cf6-2b8e-4781-a262-a9aac077694b", "starting_time": 1600, "ending_time": 1605, delay: 0, cost: 0},
	{"label":"Process Rating (Sim40)", task: "Process Rating", "id":"9e34244e-852d-4cee-b93a-f0f9fac2a784", "starting_time": 1610, "ending_time": 1615, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim41)", task: "Calculate Rating", "id":"56d03c32-dece-42be-9b12-b712067c38a0", "starting_time": 1640, "ending_time": 1645, delay: 0, cost: 0},
	{"label":"Process Rating (Sim41)", task: "Process Rating", "id":"e014d9f5-2d76-4ade-bd43-868cc8fd426a", "starting_time": 1650, "ending_time": 1655, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim42)", task: "Calculate Rating", "id":"5a0e4c51-6d5f-4978-b64e-6773596f8741", "starting_time": 1680, "ending_time": 1685, delay: 0, cost: 0},
	{"label":"Process Rating (Sim42)", task: "Process Rating", "id":"db5ae1a2-1fe5-4386-865a-51a4dd0b6468", "starting_time": 1690, "ending_time": 1695, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim43)", task: "Calculate Rating", "id":"f93a77db-8fdb-462d-ba6a-bc116e9d1fd8", "starting_time": 1720, "ending_time": 1725, delay: 0, cost: 0},
	{"label":"Process Rating (Sim43)", task: "Process Rating", "id":"f571e90a-043d-4e3d-ace6-73df7d4d4aed", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim44)", task: "Calculate Rating", "id":"e8759369-7d3f-4dbc-a94f-d232aad0e78d", "starting_time": 1760, "ending_time": 1765, delay: 0, cost: 0},
	{"label":"Process Rating (Sim44)", task: "Process Rating", "id":"eb868421-0cc4-4af0-831f-d938c6e2f48d", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim45)", task: "Calculate Rating", "id":"a3e09a27-a2c0-48d1-bb60-301b84607935", "starting_time": 1800, "ending_time": 1805, delay: 0, cost: 0},
	{"label":"Process Rating (Sim45)", task: "Process Rating", "id":"f53083aa-b12b-4c83-9a17-fa7f77855042", "starting_time": 1810, "ending_time": 1815, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim46)", task: "Calculate Rating", "id":"2051401f-3722-4038-a2b3-f37991b1cd93", "starting_time": 1840, "ending_time": 1845, delay: 0, cost: 0},
	{"label":"Process Rating (Sim46)", task: "Process Rating", "id":"bbd5af2d-b3bb-4f5f-abc9-586681b53d40", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim47)", task: "Calculate Rating", "id":"1da8027e-e806-404b-9730-06ba0d837e70", "starting_time": 1880, "ending_time": 1885, delay: 0, cost: 0},
	{"label":"Process Rating (Sim47)", task: "Process Rating", "id":"7105fd3d-558f-4f34-adea-a664805a6511", "starting_time": 1890, "ending_time": 1895, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim48)", task: "Calculate Rating", "id":"5d8b4521-3f2b-47c0-902e-2da535461ff3", "starting_time": 1920, "ending_time": 1925, delay: 0, cost: 0},
	{"label":"Process Rating (Sim48)", task: "Process Rating", "id":"b6a221f9-ee48-4bbf-a980-75ba340f51d5", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim49)", task: "Calculate Rating", "id":"082308b1-6e1c-4bef-b104-e97f97e1d190", "starting_time": 1960, "ending_time": 1965, delay: 0, cost: 0},
	{"label":"Process Rating (Sim49)", task: "Process Rating", "id":"09a9f7d8-cfdf-48ce-9553-8b3e140e5cf4", "starting_time": 1970, "ending_time": 1975, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim50)", task: "Calculate Rating", "id":"951f1651-b888-46e2-bd48-254ece8469ec", "starting_time": 2000, "ending_time": 2005, delay: 0, cost: 0},
	{"label":"Process Rating (Sim50)", task: "Process Rating", "id":"2a90e65e-0bc0-40be-9081-a868b3ab80ed", "starting_time": 2010, "ending_time": 2015, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim51)", task: "Calculate Rating", "id":"5ae500bc-aac4-4c5f-b194-fcee817674e8", "starting_time": 2040, "ending_time": 2045, delay: 0, cost: 0},
	{"label":"Process Rating (Sim51)", task: "Process Rating", "id":"85b9cdd2-4e0d-4616-9e16-bc32c2b9b6d6", "starting_time": 2045, "ending_time": 2050, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim52)", task: "Calculate Rating", "id":"28dca75b-ce63-4881-87eb-26f6f47076fa", "starting_time": 2080, "ending_time": 2085, delay: 0, cost: 0},
	{"label":"Process Rating (Sim52)", task: "Process Rating", "id":"b1e96738-7b7e-4927-8b49-4d86fdf50196", "starting_time": 2090, "ending_time": 2095, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim53)", task: "Calculate Rating", "id":"5fd2e260-744e-44a5-b937-c7fd492f965b", "starting_time": 2120, "ending_time": 2125, delay: 0, cost: 0},
	{"label":"Process Rating (Sim53)", task: "Process Rating", "id":"6eccfdc8-c525-47fb-bf9e-284a92a63043", "starting_time": 2125, "ending_time": 2130, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim54)", task: "Calculate Rating", "id":"2120e81e-c7a2-4d2a-85e0-b449741cab06", "starting_time": 2160, "ending_time": 2165, delay: 0, cost: 0},
	{"label":"Process Rating (Sim54)", task: "Process Rating", "id":"77fc8e48-2114-41eb-885e-8c3af0e6d411", "starting_time": 2165, "ending_time": 2170, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim55)", task: "Calculate Rating", "id":"dd8543c4-bf8e-4c3c-a1fc-0bc121395230", "starting_time": 2200, "ending_time": 2205, delay: 0, cost: 0},
	{"label":"Process Rating (Sim55)", task: "Process Rating", "id":"d8f75e84-98c8-4cd8-87c2-24de9c64322d", "starting_time": 2210, "ending_time": 2215, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim56)", task: "Calculate Rating", "id":"d54a296e-a6ce-449f-bb19-0fa7bd9e960b", "starting_time": 2240, "ending_time": 2245, delay: 0, cost: 0},
	{"label":"Process Rating (Sim56)", task: "Process Rating", "id":"e5028c46-e7d4-4b70-b215-9449e395e82c", "starting_time": 2250, "ending_time": 2255, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim57)", task: "Calculate Rating", "id":"bff07c7b-0197-4871-8a8f-68c0e21a0982", "starting_time": 2280, "ending_time": 2285, delay: 0, cost: 0},
	{"label":"Process Rating (Sim57)", task: "Process Rating", "id":"66db395d-0335-45a4-a0e6-2fcbc7c3ab51", "starting_time": 2290, "ending_time": 2295, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim58)", task: "Calculate Rating", "id":"80fc022d-ba8a-4a30-9db8-0565f976b33b", "starting_time": 2320, "ending_time": 2325, delay: 0, cost: 0},
	{"label":"Process Rating (Sim58)", task: "Process Rating", "id":"4d3ed267-bea7-446c-ad42-3d9243313fc1", "starting_time": 2330, "ending_time": 2335, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim59)", task: "Calculate Rating", "id":"debdad1f-aa08-4a66-b269-67a4bf4ae2c5", "starting_time": 2360, "ending_time": 2365, delay: 0, cost: 0},
	{"label":"Process Rating (Sim59)", task: "Process Rating", "id":"8c927b06-1364-450b-9743-6991567d44e6", "starting_time": 2365, "ending_time": 2370, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim60)", task: "Calculate Rating", "id":"9c9ff04f-5257-4c31-9e40-09163116130b", "starting_time": 2400, "ending_time": 2405, delay: 0, cost: 0},
	{"label":"Process Rating (Sim60)", task: "Process Rating", "id":"71ed4879-c403-43d9-8d84-58811781d1b0", "starting_time": 2405, "ending_time": 2410, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim61)", task: "Calculate Rating", "id":"5a366c28-98e1-404e-bda4-81d75ce30086", "starting_time": 2440, "ending_time": 2445, delay: 0, cost: 0},
	{"label":"Process Rating (Sim61)", task: "Process Rating", "id":"9749588f-dce7-4388-bb76-416bc6810a25", "starting_time": 2445, "ending_time": 2450, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim62)", task: "Calculate Rating", "id":"05f0f335-e3c7-4095-9a49-68eb550b4061", "starting_time": 2480, "ending_time": 2485, delay: 0, cost: 0},
	{"label":"Process Rating (Sim62)", task: "Process Rating", "id":"0fb0f1bf-14c4-4602-8e1f-b497284a1a45", "starting_time": 2485, "ending_time": 2490, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim63)", task: "Calculate Rating", "id":"622a16ee-e483-4dd2-a4f1-ecc4a339f0f7", "starting_time": 2520, "ending_time": 2525, delay: 0, cost: 0},
	{"label":"Process Rating (Sim63)", task: "Process Rating", "id":"17b94251-c3a6-447e-82db-4fa8537bb8f3", "starting_time": 2525, "ending_time": 2530, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim64)", task: "Calculate Rating", "id":"63bad6fa-d3f8-427b-b07d-7c6f4ebdbfee", "starting_time": 2560, "ending_time": 2565, delay: 0, cost: 0},
	{"label":"Process Rating (Sim64)", task: "Process Rating", "id":"d2e0b4c7-2c7e-4347-9b95-49d85f6f456b", "starting_time": 2570, "ending_time": 2575, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim65)", task: "Calculate Rating", "id":"dd103f28-0ce1-45f9-bdbc-593af13baaac", "starting_time": 2600, "ending_time": 2605, delay: 0, cost: 0},
	{"label":"Process Rating (Sim65)", task: "Process Rating", "id":"12dc8306-6ddd-4e22-8410-65c322e1e822", "starting_time": 2610, "ending_time": 2615, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim66)", task: "Calculate Rating", "id":"9889a6e4-66d0-4113-83a9-f2d46371da39", "starting_time": 2640, "ending_time": 2645, delay: 0, cost: 0},
	{"label":"Process Rating (Sim66)", task: "Process Rating", "id":"b063dd7f-a26f-4076-9544-9d2789ff4352", "starting_time": 2650, "ending_time": 2655, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim67)", task: "Calculate Rating", "id":"f6e1f9af-800d-479d-ae05-6a8d9172a593", "starting_time": 2680, "ending_time": 2685, delay: 0, cost: 0},
	{"label":"Process Rating (Sim67)", task: "Process Rating", "id":"9aed863f-a222-4869-a0f8-73a1f3bf712d", "starting_time": 2685, "ending_time": 2690, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim68)", task: "Calculate Rating", "id":"47ad15e6-c75a-41c9-ad7b-8de161bf42c4", "starting_time": 2720, "ending_time": 2725, delay: 0, cost: 0},
	{"label":"Process Rating (Sim68)", task: "Process Rating", "id":"0506d990-8cdf-4b4b-b72d-b50688949a7f", "starting_time": 2730, "ending_time": 2735, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim69)", task: "Calculate Rating", "id":"64ec0b19-ad8a-4372-b45f-88105ad9fc2a", "starting_time": 2760, "ending_time": 2765, delay: 0, cost: 0},
	{"label":"Process Rating (Sim69)", task: "Process Rating", "id":"309fb201-fdb6-44dd-9233-09cdbd7fc061", "starting_time": 2770, "ending_time": 2775, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim70)", task: "Calculate Rating", "id":"cca17725-188a-48ce-ab33-a7b1ecd04689", "starting_time": 2800, "ending_time": 2805, delay: 0, cost: 0},
	{"label":"Process Rating (Sim70)", task: "Process Rating", "id":"e993d84b-c2e8-45f1-88a6-bfd32e81eb3e", "starting_time": 2810, "ending_time": 2815, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim71)", task: "Calculate Rating", "id":"7c0fe881-13b6-4c26-af6f-6154b5fa2c9c", "starting_time": 2840, "ending_time": 2845, delay: 0, cost: 0},
	{"label":"Process Rating (Sim71)", task: "Process Rating", "id":"8d3102d3-039a-487f-b3d3-5608aa0a5313", "starting_time": 2850, "ending_time": 2855, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim72)", task: "Calculate Rating", "id":"788466d2-0bc8-4ff2-bc46-715e578dc210", "starting_time": 2880, "ending_time": 2885, delay: 0, cost: 0},
	{"label":"Process Rating (Sim72)", task: "Process Rating", "id":"ccd6a666-cbe4-4956-b9dd-e1dd1c7dff0e", "starting_time": 2890, "ending_time": 2895, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim73)", task: "Calculate Rating", "id":"ec7df998-bbae-4e76-bf34-91e1704e5ba7", "starting_time": 2920, "ending_time": 2925, delay: 0, cost: 0},
	{"label":"Process Rating (Sim73)", task: "Process Rating", "id":"0f341345-adec-4857-ba4c-141154fe49f4", "starting_time": 2930, "ending_time": 2935, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim74)", task: "Calculate Rating", "id":"a5163f8e-f0e2-4597-9a9e-65deecd1d818", "starting_time": 2960, "ending_time": 2965, delay: 0, cost: 0},
	{"label":"Process Rating (Sim74)", task: "Process Rating", "id":"875edb0e-05b9-469e-9b7b-1e764c195f79", "starting_time": 2970, "ending_time": 2975, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim75)", task: "Calculate Rating", "id":"2c74bce0-96ff-4c7c-8f53-7908cca32fc9", "starting_time": 3000, "ending_time": 3005, delay: 0, cost: 0},
	{"label":"Process Rating (Sim75)", task: "Process Rating", "id":"549f5a16-8407-40e5-b47e-fb3c0e719f29", "starting_time": 3010, "ending_time": 3015, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim76)", task: "Calculate Rating", "id":"cfaed5e4-80f8-49a3-9857-174f93a74cf2", "starting_time": 3040, "ending_time": 3045, delay: 0, cost: 0},
	{"label":"Process Rating (Sim76)", task: "Process Rating", "id":"72d3ebd1-89af-4dd8-8e60-ef4d2741c902", "starting_time": 3050, "ending_time": 3055, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim77)", task: "Calculate Rating", "id":"dac4c358-ffb5-49b9-a3b0-30a41f0e9ccf", "starting_time": 3080, "ending_time": 3085, delay: 0, cost: 0},
	{"label":"Process Rating (Sim77)", task: "Process Rating", "id":"799a018c-e50f-4da7-84bb-ecf595537cf3", "starting_time": 3090, "ending_time": 3095, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim78)", task: "Calculate Rating", "id":"85586341-77eb-41e1-824d-8eb6b148b354", "starting_time": 3120, "ending_time": 3125, delay: 0, cost: 0},
	{"label":"Process Rating (Sim78)", task: "Process Rating", "id":"7b838c9f-1900-4b9c-b80b-56a6a87b82aa", "starting_time": 3125, "ending_time": 3130, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim79)", task: "Calculate Rating", "id":"dea461d0-1400-46d9-bc92-7d374b647d00", "starting_time": 3160, "ending_time": 3165, delay: 0, cost: 0},
	{"label":"Process Rating (Sim79)", task: "Process Rating", "id":"090bd9a8-f15a-43db-891e-b90a0f21b621", "starting_time": 3165, "ending_time": 3170, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim80)", task: "Calculate Rating", "id":"6a23bfa5-01a8-4881-a64d-bdcdbbf26333", "starting_time": 3200, "ending_time": 3205, delay: 0, cost: 0},
	{"label":"Process Rating (Sim80)", task: "Process Rating", "id":"ca083d83-713f-47b2-9adc-f513a21e93bf", "starting_time": 3205, "ending_time": 3210, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim81)", task: "Calculate Rating", "id":"2b710371-c10c-476f-a0c6-3b795cc24aaf", "starting_time": 3240, "ending_time": 3245, delay: 0, cost: 0},
	{"label":"Process Rating (Sim81)", task: "Process Rating", "id":"b6a223a3-0d82-49f8-9859-787b7290a615", "starting_time": 3250, "ending_time": 3255, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim82)", task: "Calculate Rating", "id":"20d6cc3b-ec8f-458a-8837-9569c64d2b39", "starting_time": 3280, "ending_time": 3285, delay: 0, cost: 0},
	{"label":"Process Rating (Sim82)", task: "Process Rating", "id":"0737544b-fed1-4001-9e17-2f3074b486bf", "starting_time": 3285, "ending_time": 3290, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim83)", task: "Calculate Rating", "id":"622b20b2-7f9b-4913-9c53-d052660c6352", "starting_time": 3320, "ending_time": 3325, delay: 0, cost: 0},
	{"label":"Process Rating (Sim83)", task: "Process Rating", "id":"1d6cf63c-933e-44c6-b8e1-03637caa017c", "starting_time": 3330, "ending_time": 3335, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim84)", task: "Calculate Rating", "id":"10e5c840-258c-4655-9f00-ef56c9651983", "starting_time": 3360, "ending_time": 3365, delay: 0, cost: 0},
	{"label":"Process Rating (Sim84)", task: "Process Rating", "id":"7fcb0225-86e7-4ba1-ae86-1a56118a094d", "starting_time": 3370, "ending_time": 3375, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim85)", task: "Calculate Rating", "id":"168639f5-ff1b-4822-b21d-f43f8c3db336", "starting_time": 3400, "ending_time": 3405, delay: 0, cost: 0},
	{"label":"Process Rating (Sim85)", task: "Process Rating", "id":"bc3c1bc5-04ae-461c-827c-a4dc91409993", "starting_time": 3410, "ending_time": 3415, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim86)", task: "Calculate Rating", "id":"8109e237-5e71-4316-9676-764608b18b32", "starting_time": 3440, "ending_time": 3445, delay: 0, cost: 0},
	{"label":"Process Rating (Sim86)", task: "Process Rating", "id":"4398e410-8a79-4253-9ee3-6640b5960250", "starting_time": 3445, "ending_time": 3450, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim87)", task: "Calculate Rating", "id":"b698b80e-dfd9-419b-aa9c-3a0efe819e3e", "starting_time": 3480, "ending_time": 3485, delay: 0, cost: 0},
	{"label":"Process Rating (Sim87)", task: "Process Rating", "id":"1b445e95-95e1-4d6d-8e12-31af017498b0", "starting_time": 3485, "ending_time": 3490, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim88)", task: "Calculate Rating", "id":"519a776e-a4e3-4e17-928e-4c197cd47d18", "starting_time": 3520, "ending_time": 3525, delay: 0, cost: 0},
	{"label":"Process Rating (Sim88)", task: "Process Rating", "id":"66bd5fba-5834-40dc-bf5f-fca9a0c96d9e", "starting_time": 3525, "ending_time": 3530, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim89)", task: "Calculate Rating", "id":"474cb738-09dc-49f7-9c28-8482114c837c", "starting_time": 3560, "ending_time": 3565, delay: 0, cost: 0},
	{"label":"Process Rating (Sim89)", task: "Process Rating", "id":"4223ee6c-6d7b-4129-8b09-7d043674925a", "starting_time": 3570, "ending_time": 3575, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim90)", task: "Calculate Rating", "id":"a91e98cb-dbbf-46ae-a835-0546c31d9043", "starting_time": 3600, "ending_time": 3605, delay: 0, cost: 0},
	{"label":"Process Rating (Sim90)", task: "Process Rating", "id":"795d94fc-d8fa-42b1-ad2e-047749074f0f", "starting_time": 3605, "ending_time": 3610, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim91)", task: "Calculate Rating", "id":"2f160e02-1f9e-4a8c-9d86-2d618ade144c", "starting_time": 3640, "ending_time": 3645, delay: 0, cost: 0},
	{"label":"Process Rating (Sim91)", task: "Process Rating", "id":"eb1a532c-011e-4b09-9894-543a6a81a466", "starting_time": 3650, "ending_time": 3655, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim92)", task: "Calculate Rating", "id":"eb864faa-0b3b-4b99-8634-95f9d5cd6e54", "starting_time": 3680, "ending_time": 3685, delay: 0, cost: 0},
	{"label":"Process Rating (Sim92)", task: "Process Rating", "id":"ee4d34ae-64ae-4040-b542-1f6b0180cb8c", "starting_time": 3690, "ending_time": 3695, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim93)", task: "Calculate Rating", "id":"e6b4895e-67e8-48c9-aa42-e50492c7bdc8", "starting_time": 3720, "ending_time": 3725, delay: 0, cost: 0},
	{"label":"Process Rating (Sim93)", task: "Process Rating", "id":"605c815b-4c0d-4784-8314-7396ecb0413d", "starting_time": 3725, "ending_time": 3730, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim94)", task: "Calculate Rating", "id":"659f5929-885b-4001-8e26-ac1283c7ffb0", "starting_time": 3760, "ending_time": 3765, delay: 0, cost: 0},
	{"label":"Process Rating (Sim94)", task: "Process Rating", "id":"526aa4ee-315e-44d8-aa3e-a4304ea7c58d", "starting_time": 3770, "ending_time": 3775, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim95)", task: "Calculate Rating", "id":"c1df70e3-df67-4d1d-a448-3c1d0a440f21", "starting_time": 3800, "ending_time": 3805, delay: 0, cost: 0},
	{"label":"Process Rating (Sim95)", task: "Process Rating", "id":"6b596eae-6756-4eeb-b6c5-6247a056c364", "starting_time": 3810, "ending_time": 3815, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim96)", task: "Calculate Rating", "id":"4fa8fdaa-eb50-4748-bcac-7ae306e297c4", "starting_time": 3840, "ending_time": 3845, delay: 0, cost: 0},
	{"label":"Process Rating (Sim96)", task: "Process Rating", "id":"d8c63a69-3150-498b-bc33-35911169cf5f", "starting_time": 3845, "ending_time": 3850, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim97)", task: "Calculate Rating", "id":"0a0b566b-be74-4ff0-9de8-b526437f7bf3", "starting_time": 3880, "ending_time": 3885, delay: 0, cost: 0},
	{"label":"Process Rating (Sim97)", task: "Process Rating", "id":"e344c1d2-0124-4ad0-85c5-6f718fe36dc6", "starting_time": 3885, "ending_time": 3890, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim98)", task: "Calculate Rating", "id":"21b1885e-f13e-48af-8a35-ae93488d650a", "starting_time": 3920, "ending_time": 3925, delay: 0, cost: 0},
	{"label":"Process Rating (Sim98)", task: "Process Rating", "id":"1f84860b-54f8-4d45-b7c0-3ed25e5dc655", "starting_time": 3930, "ending_time": 3935, delay: 0, cost: 0},
	{"label":"Calculate Rating (Sim99)", task: "Calculate Rating", "id":"97982a07-9004-4c59-973c-fe291ece5ec5", "starting_time": 3960, "ending_time": 3965, delay: 0, cost: 0},
	{"label":"Process Rating (Sim99)", task: "Process Rating", "id":"6cdc7b5b-25a9-401b-9213-820d070d772c", "starting_time": 3970, "ending_time": 3975, delay: 0, cost: 0}
]},
{label: "r2", times: [
	{"label":"Calculate Rating Manually (Sim1)", task: "Calculate Rating Manually", "id":"697676ff-1f9f-44fd-9d2d-a4a3dbb1ade0", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim2)", task: "Calculate Rating Manually", "id":"9a72d273-8fe5-4c5e-be1e-8495dae60a35", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim4)", task: "Calculate Rating Manually", "id":"cf595afd-ec9a-4018-88ca-3c69d5da7ea1", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim7)", task: "Calculate Rating Manually", "id":"491aa45b-17c6-489d-9035-2ee0e900bbe6", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim9)", task: "Calculate Rating Manually", "id":"6050de2d-ee90-4cbe-be31-454838fe5e12", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim13)", task: "Calculate Rating Manually", "id":"21e0b170-096a-4299-b9ca-6ad1ae9a2d45", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim20)", task: "Calculate Rating Manually", "id":"78d194ba-f239-48a0-9c09-030da81bf0ec", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim24)", task: "Calculate Rating Manually", "id":"6f19c668-e4d4-4425-a905-45ccb8e7a234", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim25)", task: "Calculate Rating Manually", "id":"49e517af-2a2a-433b-940b-bc8584a568e4", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim29)", task: "Calculate Rating Manually", "id":"c0213049-4766-4166-81cc-deaa2dea4664", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim30)", task: "Calculate Rating Manually", "id":"99ed138e-f868-422c-9272-dda9282ddcd2", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim34)", task: "Calculate Rating Manually", "id":"f7a19fd6-8b5f-47d9-8c53-5b70bffcb357", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim35)", task: "Calculate Rating Manually", "id":"f713d51a-c573-4c00-b0f9-fd2f22c6d7b4", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim38)", task: "Calculate Rating Manually", "id":"0274cbae-1578-4323-85ca-e8773012f17d", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim40)", task: "Calculate Rating Manually", "id":"57813a96-9a0d-4c2b-aa63-bb7c666728dd", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim41)", task: "Calculate Rating Manually", "id":"636d1a8d-e30e-4ae4-8416-d68081d43c53", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim42)", task: "Calculate Rating Manually", "id":"b81c4a27-a6fa-43cd-8c52-9087903ec07d", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim45)", task: "Calculate Rating Manually", "id":"c4baf3a7-f620-436b-a4a0-f2f1cadfa97f", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim47)", task: "Calculate Rating Manually", "id":"463a40fc-1ce8-4a99-bbf3-67a334d503c3", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim49)", task: "Calculate Rating Manually", "id":"6dda29aa-a2e9-4886-9056-a5b3b00d445c", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim50)", task: "Calculate Rating Manually", "id":"3d520bc1-3c70-49eb-a628-74673d84630c", "starting_time": 2005, "ending_time": 2010, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim52)", task: "Calculate Rating Manually", "id":"62fa629d-1d9a-4ce8-9a31-9497d92ccc38", "starting_time": 2085, "ending_time": 2090, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim55)", task: "Calculate Rating Manually", "id":"9a1fb8ab-60d7-4006-85c0-45a48cf5a9b2", "starting_time": 2205, "ending_time": 2210, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim56)", task: "Calculate Rating Manually", "id":"b28c2452-f058-4aef-9b5d-99dbb08d002a", "starting_time": 2245, "ending_time": 2250, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim57)", task: "Calculate Rating Manually", "id":"28492a6a-e00f-495f-8c86-1a426735b67c", "starting_time": 2285, "ending_time": 2290, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim58)", task: "Calculate Rating Manually", "id":"8287c909-28be-4090-a60f-c83b2d12a811", "starting_time": 2325, "ending_time": 2330, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim64)", task: "Calculate Rating Manually", "id":"0e3da255-1dda-4e20-86d1-2c57b2bc3f91", "starting_time": 2565, "ending_time": 2570, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim65)", task: "Calculate Rating Manually", "id":"45874f69-fef6-4d3b-af67-26485821894a", "starting_time": 2605, "ending_time": 2610, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim66)", task: "Calculate Rating Manually", "id":"007773a7-37b0-4b5e-b39c-65fae3237b12", "starting_time": 2645, "ending_time": 2650, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim68)", task: "Calculate Rating Manually", "id":"2ae35e75-50ec-4acf-957d-7e3fef5a3c0d", "starting_time": 2725, "ending_time": 2730, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim69)", task: "Calculate Rating Manually", "id":"45e7978d-a2ef-4f2a-bc5a-f3d0dd78f04b", "starting_time": 2765, "ending_time": 2770, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim70)", task: "Calculate Rating Manually", "id":"1d5e4e7b-377a-4872-9ff4-ffb3adfb7ea3", "starting_time": 2805, "ending_time": 2810, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim71)", task: "Calculate Rating Manually", "id":"33623b0a-1c50-40a2-ad8d-b980cfcf442c", "starting_time": 2845, "ending_time": 2850, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim72)", task: "Calculate Rating Manually", "id":"0f478660-a43e-4d13-9eef-3a109e6115c7", "starting_time": 2885, "ending_time": 2890, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim73)", task: "Calculate Rating Manually", "id":"38569864-0c72-4319-89fc-fd94c8769232", "starting_time": 2925, "ending_time": 2930, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim74)", task: "Calculate Rating Manually", "id":"7d2ea18b-b85d-4e01-81bf-60ddd91c2236", "starting_time": 2965, "ending_time": 2970, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim75)", task: "Calculate Rating Manually", "id":"0f105135-5a5e-42f3-bad8-92a21a5c6369", "starting_time": 3005, "ending_time": 3010, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim76)", task: "Calculate Rating Manually", "id":"5760c947-8a64-4730-8656-f57dbd7ecf0c", "starting_time": 3045, "ending_time": 3050, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim77)", task: "Calculate Rating Manually", "id":"0f02b89a-857a-4d09-acd5-9075751e4877", "starting_time": 3085, "ending_time": 3090, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim81)", task: "Calculate Rating Manually", "id":"ad00cf91-11b2-4590-a7eb-06dc203c797a", "starting_time": 3245, "ending_time": 3250, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim83)", task: "Calculate Rating Manually", "id":"ca2195d8-214d-4e0c-ad7b-2925f7b91756", "starting_time": 3325, "ending_time": 3330, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim84)", task: "Calculate Rating Manually", "id":"17643dba-d058-40f3-8497-355736361266", "starting_time": 3365, "ending_time": 3370, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim85)", task: "Calculate Rating Manually", "id":"9104cf5e-7724-4c90-b2e8-2ee5ecd30355", "starting_time": 3405, "ending_time": 3410, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim89)", task: "Calculate Rating Manually", "id":"9be137bf-4458-4879-9313-a4775c67ce27", "starting_time": 3565, "ending_time": 3570, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim91)", task: "Calculate Rating Manually", "id":"f939be19-42cc-4f7c-9dc5-bbe9553053c7", "starting_time": 3645, "ending_time": 3650, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim92)", task: "Calculate Rating Manually", "id":"840830c2-6c04-4ce1-95b1-2bf63fef9023", "starting_time": 3685, "ending_time": 3690, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim94)", task: "Calculate Rating Manually", "id":"42232b75-70ed-45cd-b9d9-0755a80e988c", "starting_time": 3765, "ending_time": 3770, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim95)", task: "Calculate Rating Manually", "id":"9c3e13c2-63fb-43f3-a2d4-af80b60a7ef9", "starting_time": 3805, "ending_time": 3810, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim98)", task: "Calculate Rating Manually", "id":"44aae2a7-a625-44cf-b006-0f3207763638", "starting_time": 3925, "ending_time": 3930, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim99)", task: "Calculate Rating Manually", "id":"26581c48-d895-4fa1-9fd5-a1e86a7a62c1", "starting_time": 3965, "ending_time": 3970, delay: 0, cost: 0}
]},
{label: "r3", times: [

]},
{label: "r4", times: [

]},
];

var simulationData = [
{label: "Sim0", times: [
	{"label":"Calculate Rating (Sim0)", task: "Calculate Rating", "id":"4a9c28eb-374a-42b5-8944-303dd4909319", "starting_time": 0, "ending_time": 5, delay: 0, cost: 0},
	{"label":"Process Rating (Sim0)", task: "Process Rating", "id":"d2d538e6-4e96-4226-a614-8c57b888c271", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0}
]},
{label: "Sim1", times: [
	{"label":"Calculate Rating (Sim1)", task: "Calculate Rating", "id":"5c62957e-cae3-4412-89d5-ee424005b7aa", "starting_time": 40, "ending_time": 45, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim1)", task: "Calculate Rating Manually", "id":"697676ff-1f9f-44fd-9d2d-a4a3dbb1ade0", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"Process Rating (Sim1)", task: "Process Rating", "id":"18f26aec-f1ba-49e8-b71a-cf02b2b4889e", "starting_time": 50, "ending_time": 55, delay: 0, cost: 0}
]},
{label: "Sim10", times: [
	{"label":"Calculate Rating (Sim10)", task: "Calculate Rating", "id":"636f7ed9-79bb-4169-9529-3ee6e6974969", "starting_time": 400, "ending_time": 405, delay: 0, cost: 0},
	{"label":"Process Rating (Sim10)", task: "Process Rating", "id":"ef12b8de-80bf-4646-abcc-825bbbf0f7e2", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0}
]},
{label: "Sim11", times: [
	{"label":"Calculate Rating (Sim11)", task: "Calculate Rating", "id":"79324154-95cb-4318-8dea-f64ebc5ec3a3", "starting_time": 440, "ending_time": 445, delay: 0, cost: 0},
	{"label":"Process Rating (Sim11)", task: "Process Rating", "id":"13a1882e-2a8b-462c-b484-607ecb9a42ff", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0}
]},
{label: "Sim12", times: [
	{"label":"Calculate Rating (Sim12)", task: "Calculate Rating", "id":"9fe557ac-8604-474a-a4d8-33eaa782368e", "starting_time": 480, "ending_time": 485, delay: 0, cost: 0},
	{"label":"Process Rating (Sim12)", task: "Process Rating", "id":"abf0bddb-d00c-4aed-b370-284b9f5f6e9b", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0}
]},
{label: "Sim13", times: [
	{"label":"Calculate Rating (Sim13)", task: "Calculate Rating", "id":"c7c8de74-d7bf-48be-ba51-cac041ead318", "starting_time": 520, "ending_time": 525, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim13)", task: "Calculate Rating Manually", "id":"21e0b170-096a-4299-b9ca-6ad1ae9a2d45", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"Process Rating (Sim13)", task: "Process Rating", "id":"6325335c-b457-4ddc-8288-44d6218a795b", "starting_time": 530, "ending_time": 535, delay: 0, cost: 0}
]},
{label: "Sim14", times: [
	{"label":"Calculate Rating (Sim14)", task: "Calculate Rating", "id":"dd2332be-dd5c-4532-a8ff-8a935cb954fe", "starting_time": 560, "ending_time": 565, delay: 0, cost: 0},
	{"label":"Process Rating (Sim14)", task: "Process Rating", "id":"d40cd54c-82f5-42e0-9f4a-2ad8fccb8b45", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0}
]},
{label: "Sim15", times: [
	{"label":"Calculate Rating (Sim15)", task: "Calculate Rating", "id":"aad7187c-6356-4ad5-8e67-b467b3d89b70", "starting_time": 600, "ending_time": 605, delay: 0, cost: 0},
	{"label":"Process Rating (Sim15)", task: "Process Rating", "id":"b994499a-f863-4955-97c5-184decf8e64e", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0}
]},
{label: "Sim16", times: [
	{"label":"Calculate Rating (Sim16)", task: "Calculate Rating", "id":"a9f3ca6b-e8f5-4f31-b44c-6a08fcc1f85c", "starting_time": 640, "ending_time": 645, delay: 0, cost: 0},
	{"label":"Process Rating (Sim16)", task: "Process Rating", "id":"458140ab-27bd-4df4-9538-8473850af7b0", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0}
]},
{label: "Sim17", times: [
	{"label":"Calculate Rating (Sim17)", task: "Calculate Rating", "id":"9a9eec26-f80d-45d9-96b0-c935642b52a1", "starting_time": 680, "ending_time": 685, delay: 0, cost: 0},
	{"label":"Process Rating (Sim17)", task: "Process Rating", "id":"65bc4797-ed87-4380-8032-3415eb2c42bc", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0}
]},
{label: "Sim18", times: [
	{"label":"Calculate Rating (Sim18)", task: "Calculate Rating", "id":"b5ef88fa-7b1a-4e05-b1d2-8f984cfa614d", "starting_time": 720, "ending_time": 725, delay: 0, cost: 0},
	{"label":"Process Rating (Sim18)", task: "Process Rating", "id":"753dca36-7422-4a5d-a605-7e5e5fc1158b", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0}
]},
{label: "Sim19", times: [
	{"label":"Calculate Rating (Sim19)", task: "Calculate Rating", "id":"870455ee-8370-40dc-bece-445ef4d55b69", "starting_time": 760, "ending_time": 765, delay: 0, cost: 0},
	{"label":"Process Rating (Sim19)", task: "Process Rating", "id":"2d2d55f2-85eb-4c7c-8387-e0f8aa1c697e", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0}
]},
{label: "Sim2", times: [
	{"label":"Calculate Rating (Sim2)", task: "Calculate Rating", "id":"ed785ffc-498e-474d-a210-4306baa9d1fd", "starting_time": 80, "ending_time": 85, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim2)", task: "Calculate Rating Manually", "id":"9a72d273-8fe5-4c5e-be1e-8495dae60a35", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"Process Rating (Sim2)", task: "Process Rating", "id":"bed74714-ea7e-41d9-983d-23cd64bbc525", "starting_time": 90, "ending_time": 95, delay: 0, cost: 0}
]},
{label: "Sim20", times: [
	{"label":"Calculate Rating (Sim20)", task: "Calculate Rating", "id":"94103fde-7159-434b-9dbf-21a4df832663", "starting_time": 800, "ending_time": 805, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim20)", task: "Calculate Rating Manually", "id":"78d194ba-f239-48a0-9c09-030da81bf0ec", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"Process Rating (Sim20)", task: "Process Rating", "id":"ebbd548d-d62a-4527-a7bc-b8baa859587e", "starting_time": 810, "ending_time": 815, delay: 0, cost: 0}
]},
{label: "Sim21", times: [
	{"label":"Calculate Rating (Sim21)", task: "Calculate Rating", "id":"d6dd50f0-cc18-4ab3-bb75-6606bf2144ad", "starting_time": 840, "ending_time": 845, delay: 0, cost: 0},
	{"label":"Process Rating (Sim21)", task: "Process Rating", "id":"e3d5dd19-5eca-4bc6-b128-b497afac47e4", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0}
]},
{label: "Sim22", times: [
	{"label":"Calculate Rating (Sim22)", task: "Calculate Rating", "id":"dc3e646f-b0f5-4c36-8a08-2b92d89f2088", "starting_time": 880, "ending_time": 885, delay: 0, cost: 0},
	{"label":"Process Rating (Sim22)", task: "Process Rating", "id":"70dd5aa4-ffb6-42d6-9155-38abcfb1978d", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0}
]},
{label: "Sim23", times: [
	{"label":"Calculate Rating (Sim23)", task: "Calculate Rating", "id":"7548ba63-1ee8-4065-bee9-7ce991cf6f01", "starting_time": 920, "ending_time": 925, delay: 0, cost: 0},
	{"label":"Process Rating (Sim23)", task: "Process Rating", "id":"1cc8de9a-388b-49f0-b50c-f0fb0ae2aade", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0}
]},
{label: "Sim24", times: [
	{"label":"Calculate Rating (Sim24)", task: "Calculate Rating", "id":"e707c55e-48d7-413b-b78a-29600f70be4e", "starting_time": 960, "ending_time": 965, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim24)", task: "Calculate Rating Manually", "id":"6f19c668-e4d4-4425-a905-45ccb8e7a234", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"Process Rating (Sim24)", task: "Process Rating", "id":"abaf1919-db99-4286-9248-1fb518de1db4", "starting_time": 970, "ending_time": 975, delay: 0, cost: 0}
]},
{label: "Sim25", times: [
	{"label":"Calculate Rating (Sim25)", task: "Calculate Rating", "id":"8d6348c0-2dbe-4e4f-95d7-8fe6d32be03d", "starting_time": 1000, "ending_time": 1005, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim25)", task: "Calculate Rating Manually", "id":"49e517af-2a2a-433b-940b-bc8584a568e4", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"Process Rating (Sim25)", task: "Process Rating", "id":"75c1507f-df72-4ea9-bbb9-79e51e59be6a", "starting_time": 1010, "ending_time": 1015, delay: 0, cost: 0}
]},
{label: "Sim26", times: [
	{"label":"Calculate Rating (Sim26)", task: "Calculate Rating", "id":"7f2f056b-4a77-4241-a94b-f54f9227be64", "starting_time": 1040, "ending_time": 1045, delay: 0, cost: 0},
	{"label":"Process Rating (Sim26)", task: "Process Rating", "id":"01ee3717-e26f-4ddd-8e7a-127c87c8cf87", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0}
]},
{label: "Sim27", times: [
	{"label":"Calculate Rating (Sim27)", task: "Calculate Rating", "id":"26cb41dc-bc5b-413a-ac89-9fea69d1745b", "starting_time": 1080, "ending_time": 1085, delay: 0, cost: 0},
	{"label":"Process Rating (Sim27)", task: "Process Rating", "id":"ab0fcad6-201e-451c-8bce-f12e654931dc", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0}
]},
{label: "Sim28", times: [
	{"label":"Calculate Rating (Sim28)", task: "Calculate Rating", "id":"982ce78c-684c-4248-a876-abbf003171ae", "starting_time": 1120, "ending_time": 1125, delay: 0, cost: 0},
	{"label":"Process Rating (Sim28)", task: "Process Rating", "id":"b7623fa9-cc31-46a9-9281-cdd5b870f5ef", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0}
]},
{label: "Sim29", times: [
	{"label":"Calculate Rating (Sim29)", task: "Calculate Rating", "id":"a68463ec-46aa-47c2-930b-ec7fd4fa8808", "starting_time": 1160, "ending_time": 1165, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim29)", task: "Calculate Rating Manually", "id":"c0213049-4766-4166-81cc-deaa2dea4664", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"Process Rating (Sim29)", task: "Process Rating", "id":"5c4471c0-3fb0-402c-a746-00925d5e06ea", "starting_time": 1170, "ending_time": 1175, delay: 0, cost: 0}
]},
{label: "Sim3", times: [
	{"label":"Calculate Rating (Sim3)", task: "Calculate Rating", "id":"fb54ac2f-c65a-4f70-8776-1ef3b45361ad", "starting_time": 120, "ending_time": 125, delay: 0, cost: 0},
	{"label":"Process Rating (Sim3)", task: "Process Rating", "id":"4f16e4be-2ef3-43c7-93bd-221aa56b2077", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0}
]},
{label: "Sim30", times: [
	{"label":"Calculate Rating (Sim30)", task: "Calculate Rating", "id":"b379a394-fb04-4564-8090-52628c09aee1", "starting_time": 1200, "ending_time": 1205, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim30)", task: "Calculate Rating Manually", "id":"99ed138e-f868-422c-9272-dda9282ddcd2", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"Process Rating (Sim30)", task: "Process Rating", "id":"9b3d1179-12b2-4bed-8eb4-8734bb5f4f36", "starting_time": 1210, "ending_time": 1215, delay: 0, cost: 0}
]},
{label: "Sim31", times: [
	{"label":"Calculate Rating (Sim31)", task: "Calculate Rating", "id":"abe65871-e029-44c8-8ca4-adad8fb9ae36", "starting_time": 1240, "ending_time": 1245, delay: 0, cost: 0},
	{"label":"Process Rating (Sim31)", task: "Process Rating", "id":"b0b2bbf5-17bf-4ed6-86e3-efa322516aca", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0}
]},
{label: "Sim32", times: [
	{"label":"Calculate Rating (Sim32)", task: "Calculate Rating", "id":"0f6c5452-4135-4d78-a966-5256746f5aa2", "starting_time": 1280, "ending_time": 1285, delay: 0, cost: 0},
	{"label":"Process Rating (Sim32)", task: "Process Rating", "id":"71d4da8b-940d-46e5-81fa-86cb1caf0614", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0}
]},
{label: "Sim33", times: [
	{"label":"Calculate Rating (Sim33)", task: "Calculate Rating", "id":"c515b963-45b4-416d-abd0-251fb5978f13", "starting_time": 1320, "ending_time": 1325, delay: 0, cost: 0},
	{"label":"Process Rating (Sim33)", task: "Process Rating", "id":"08789fc3-a7b9-407b-b76f-fc2367ed132b", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0}
]},
{label: "Sim34", times: [
	{"label":"Calculate Rating (Sim34)", task: "Calculate Rating", "id":"a86631ad-4b33-436d-ae6f-dfebd92e28d3", "starting_time": 1360, "ending_time": 1365, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim34)", task: "Calculate Rating Manually", "id":"f7a19fd6-8b5f-47d9-8c53-5b70bffcb357", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"Process Rating (Sim34)", task: "Process Rating", "id":"88dd87db-26f7-4efe-9f82-588a80339e5c", "starting_time": 1370, "ending_time": 1375, delay: 0, cost: 0}
]},
{label: "Sim35", times: [
	{"label":"Calculate Rating (Sim35)", task: "Calculate Rating", "id":"8b6d84a8-f668-4d28-b14a-c0b566c2b336", "starting_time": 1400, "ending_time": 1405, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim35)", task: "Calculate Rating Manually", "id":"f713d51a-c573-4c00-b0f9-fd2f22c6d7b4", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"Process Rating (Sim35)", task: "Process Rating", "id":"44e3e603-4eaf-44b4-b790-fe7e063ec8d5", "starting_time": 1410, "ending_time": 1415, delay: 0, cost: 0}
]},
{label: "Sim36", times: [
	{"label":"Calculate Rating (Sim36)", task: "Calculate Rating", "id":"8bb75cbf-2e08-42cb-b282-044bcb5b9a01", "starting_time": 1440, "ending_time": 1445, delay: 0, cost: 0},
	{"label":"Process Rating (Sim36)", task: "Process Rating", "id":"fef4e5f9-580c-43f3-a3f9-fc6ab3c61275", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0}
]},
{label: "Sim37", times: [
	{"label":"Calculate Rating (Sim37)", task: "Calculate Rating", "id":"be05cbb1-e4e3-4596-a741-b4616264b174", "starting_time": 1480, "ending_time": 1485, delay: 0, cost: 0},
	{"label":"Process Rating (Sim37)", task: "Process Rating", "id":"80481a7e-816a-4564-916b-00120eda17c1", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0}
]},
{label: "Sim38", times: [
	{"label":"Calculate Rating (Sim38)", task: "Calculate Rating", "id":"0df99a97-24e6-4dca-a9e8-0f777121f327", "starting_time": 1520, "ending_time": 1525, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim38)", task: "Calculate Rating Manually", "id":"0274cbae-1578-4323-85ca-e8773012f17d", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"Process Rating (Sim38)", task: "Process Rating", "id":"027e69ba-5258-4558-ad37-37247d8747d4", "starting_time": 1530, "ending_time": 1535, delay: 0, cost: 0}
]},
{label: "Sim39", times: [
	{"label":"Calculate Rating (Sim39)", task: "Calculate Rating", "id":"80749d61-958b-4556-b5c7-9c1b0e54bb8b", "starting_time": 1560, "ending_time": 1565, delay: 0, cost: 0},
	{"label":"Process Rating (Sim39)", task: "Process Rating", "id":"d9b30875-8fb1-46d9-83cc-ce28e13bab71", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0}
]},
{label: "Sim4", times: [
	{"label":"Calculate Rating (Sim4)", task: "Calculate Rating", "id":"ad152378-aa49-4bc4-9159-6c9b9afe3258", "starting_time": 160, "ending_time": 165, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim4)", task: "Calculate Rating Manually", "id":"cf595afd-ec9a-4018-88ca-3c69d5da7ea1", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"Process Rating (Sim4)", task: "Process Rating", "id":"b9cfa507-0cf3-4bfd-9cf2-643d7c7fc132", "starting_time": 170, "ending_time": 175, delay: 0, cost: 0}
]},
{label: "Sim40", times: [
	{"label":"Calculate Rating (Sim40)", task: "Calculate Rating", "id":"20c96cf6-2b8e-4781-a262-a9aac077694b", "starting_time": 1600, "ending_time": 1605, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim40)", task: "Calculate Rating Manually", "id":"57813a96-9a0d-4c2b-aa63-bb7c666728dd", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"Process Rating (Sim40)", task: "Process Rating", "id":"9e34244e-852d-4cee-b93a-f0f9fac2a784", "starting_time": 1610, "ending_time": 1615, delay: 0, cost: 0}
]},
{label: "Sim41", times: [
	{"label":"Calculate Rating (Sim41)", task: "Calculate Rating", "id":"56d03c32-dece-42be-9b12-b712067c38a0", "starting_time": 1640, "ending_time": 1645, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim41)", task: "Calculate Rating Manually", "id":"636d1a8d-e30e-4ae4-8416-d68081d43c53", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"Process Rating (Sim41)", task: "Process Rating", "id":"e014d9f5-2d76-4ade-bd43-868cc8fd426a", "starting_time": 1650, "ending_time": 1655, delay: 0, cost: 0}
]},
{label: "Sim42", times: [
	{"label":"Calculate Rating (Sim42)", task: "Calculate Rating", "id":"5a0e4c51-6d5f-4978-b64e-6773596f8741", "starting_time": 1680, "ending_time": 1685, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim42)", task: "Calculate Rating Manually", "id":"b81c4a27-a6fa-43cd-8c52-9087903ec07d", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"Process Rating (Sim42)", task: "Process Rating", "id":"db5ae1a2-1fe5-4386-865a-51a4dd0b6468", "starting_time": 1690, "ending_time": 1695, delay: 0, cost: 0}
]},
{label: "Sim43", times: [
	{"label":"Calculate Rating (Sim43)", task: "Calculate Rating", "id":"f93a77db-8fdb-462d-ba6a-bc116e9d1fd8", "starting_time": 1720, "ending_time": 1725, delay: 0, cost: 0},
	{"label":"Process Rating (Sim43)", task: "Process Rating", "id":"f571e90a-043d-4e3d-ace6-73df7d4d4aed", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0}
]},
{label: "Sim44", times: [
	{"label":"Calculate Rating (Sim44)", task: "Calculate Rating", "id":"e8759369-7d3f-4dbc-a94f-d232aad0e78d", "starting_time": 1760, "ending_time": 1765, delay: 0, cost: 0},
	{"label":"Process Rating (Sim44)", task: "Process Rating", "id":"eb868421-0cc4-4af0-831f-d938c6e2f48d", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0}
]},
{label: "Sim45", times: [
	{"label":"Calculate Rating (Sim45)", task: "Calculate Rating", "id":"a3e09a27-a2c0-48d1-bb60-301b84607935", "starting_time": 1800, "ending_time": 1805, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim45)", task: "Calculate Rating Manually", "id":"c4baf3a7-f620-436b-a4a0-f2f1cadfa97f", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"Process Rating (Sim45)", task: "Process Rating", "id":"f53083aa-b12b-4c83-9a17-fa7f77855042", "starting_time": 1810, "ending_time": 1815, delay: 0, cost: 0}
]},
{label: "Sim46", times: [
	{"label":"Calculate Rating (Sim46)", task: "Calculate Rating", "id":"2051401f-3722-4038-a2b3-f37991b1cd93", "starting_time": 1840, "ending_time": 1845, delay: 0, cost: 0},
	{"label":"Process Rating (Sim46)", task: "Process Rating", "id":"bbd5af2d-b3bb-4f5f-abc9-586681b53d40", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0}
]},
{label: "Sim47", times: [
	{"label":"Calculate Rating (Sim47)", task: "Calculate Rating", "id":"1da8027e-e806-404b-9730-06ba0d837e70", "starting_time": 1880, "ending_time": 1885, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim47)", task: "Calculate Rating Manually", "id":"463a40fc-1ce8-4a99-bbf3-67a334d503c3", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"Process Rating (Sim47)", task: "Process Rating", "id":"7105fd3d-558f-4f34-adea-a664805a6511", "starting_time": 1890, "ending_time": 1895, delay: 0, cost: 0}
]},
{label: "Sim48", times: [
	{"label":"Calculate Rating (Sim48)", task: "Calculate Rating", "id":"5d8b4521-3f2b-47c0-902e-2da535461ff3", "starting_time": 1920, "ending_time": 1925, delay: 0, cost: 0},
	{"label":"Process Rating (Sim48)", task: "Process Rating", "id":"b6a221f9-ee48-4bbf-a980-75ba340f51d5", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0}
]},
{label: "Sim49", times: [
	{"label":"Calculate Rating (Sim49)", task: "Calculate Rating", "id":"082308b1-6e1c-4bef-b104-e97f97e1d190", "starting_time": 1960, "ending_time": 1965, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim49)", task: "Calculate Rating Manually", "id":"6dda29aa-a2e9-4886-9056-a5b3b00d445c", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"Process Rating (Sim49)", task: "Process Rating", "id":"09a9f7d8-cfdf-48ce-9553-8b3e140e5cf4", "starting_time": 1970, "ending_time": 1975, delay: 0, cost: 0}
]},
{label: "Sim5", times: [
	{"label":"Calculate Rating (Sim5)", task: "Calculate Rating", "id":"7a4bd310-889f-4aba-ae7b-200685e06213", "starting_time": 200, "ending_time": 205, delay: 0, cost: 0},
	{"label":"Process Rating (Sim5)", task: "Process Rating", "id":"ca5d5092-bfe7-4b3a-a610-eb8d46c5e70b", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0}
]},
{label: "Sim50", times: [
	{"label":"Calculate Rating (Sim50)", task: "Calculate Rating", "id":"951f1651-b888-46e2-bd48-254ece8469ec", "starting_time": 2000, "ending_time": 2005, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim50)", task: "Calculate Rating Manually", "id":"3d520bc1-3c70-49eb-a628-74673d84630c", "starting_time": 2005, "ending_time": 2010, delay: 0, cost: 0},
	{"label":"Process Rating (Sim50)", task: "Process Rating", "id":"2a90e65e-0bc0-40be-9081-a868b3ab80ed", "starting_time": 2010, "ending_time": 2015, delay: 0, cost: 0}
]},
{label: "Sim51", times: [
	{"label":"Calculate Rating (Sim51)", task: "Calculate Rating", "id":"5ae500bc-aac4-4c5f-b194-fcee817674e8", "starting_time": 2040, "ending_time": 2045, delay: 0, cost: 0},
	{"label":"Process Rating (Sim51)", task: "Process Rating", "id":"85b9cdd2-4e0d-4616-9e16-bc32c2b9b6d6", "starting_time": 2045, "ending_time": 2050, delay: 0, cost: 0}
]},
{label: "Sim52", times: [
	{"label":"Calculate Rating (Sim52)", task: "Calculate Rating", "id":"28dca75b-ce63-4881-87eb-26f6f47076fa", "starting_time": 2080, "ending_time": 2085, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim52)", task: "Calculate Rating Manually", "id":"62fa629d-1d9a-4ce8-9a31-9497d92ccc38", "starting_time": 2085, "ending_time": 2090, delay: 0, cost: 0},
	{"label":"Process Rating (Sim52)", task: "Process Rating", "id":"b1e96738-7b7e-4927-8b49-4d86fdf50196", "starting_time": 2090, "ending_time": 2095, delay: 0, cost: 0}
]},
{label: "Sim53", times: [
	{"label":"Calculate Rating (Sim53)", task: "Calculate Rating", "id":"5fd2e260-744e-44a5-b937-c7fd492f965b", "starting_time": 2120, "ending_time": 2125, delay: 0, cost: 0},
	{"label":"Process Rating (Sim53)", task: "Process Rating", "id":"6eccfdc8-c525-47fb-bf9e-284a92a63043", "starting_time": 2125, "ending_time": 2130, delay: 0, cost: 0}
]},
{label: "Sim54", times: [
	{"label":"Calculate Rating (Sim54)", task: "Calculate Rating", "id":"2120e81e-c7a2-4d2a-85e0-b449741cab06", "starting_time": 2160, "ending_time": 2165, delay: 0, cost: 0},
	{"label":"Process Rating (Sim54)", task: "Process Rating", "id":"77fc8e48-2114-41eb-885e-8c3af0e6d411", "starting_time": 2165, "ending_time": 2170, delay: 0, cost: 0}
]},
{label: "Sim55", times: [
	{"label":"Calculate Rating (Sim55)", task: "Calculate Rating", "id":"dd8543c4-bf8e-4c3c-a1fc-0bc121395230", "starting_time": 2200, "ending_time": 2205, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim55)", task: "Calculate Rating Manually", "id":"9a1fb8ab-60d7-4006-85c0-45a48cf5a9b2", "starting_time": 2205, "ending_time": 2210, delay: 0, cost: 0},
	{"label":"Process Rating (Sim55)", task: "Process Rating", "id":"d8f75e84-98c8-4cd8-87c2-24de9c64322d", "starting_time": 2210, "ending_time": 2215, delay: 0, cost: 0}
]},
{label: "Sim56", times: [
	{"label":"Calculate Rating (Sim56)", task: "Calculate Rating", "id":"d54a296e-a6ce-449f-bb19-0fa7bd9e960b", "starting_time": 2240, "ending_time": 2245, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim56)", task: "Calculate Rating Manually", "id":"b28c2452-f058-4aef-9b5d-99dbb08d002a", "starting_time": 2245, "ending_time": 2250, delay: 0, cost: 0},
	{"label":"Process Rating (Sim56)", task: "Process Rating", "id":"e5028c46-e7d4-4b70-b215-9449e395e82c", "starting_time": 2250, "ending_time": 2255, delay: 0, cost: 0}
]},
{label: "Sim57", times: [
	{"label":"Calculate Rating (Sim57)", task: "Calculate Rating", "id":"bff07c7b-0197-4871-8a8f-68c0e21a0982", "starting_time": 2280, "ending_time": 2285, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim57)", task: "Calculate Rating Manually", "id":"28492a6a-e00f-495f-8c86-1a426735b67c", "starting_time": 2285, "ending_time": 2290, delay: 0, cost: 0},
	{"label":"Process Rating (Sim57)", task: "Process Rating", "id":"66db395d-0335-45a4-a0e6-2fcbc7c3ab51", "starting_time": 2290, "ending_time": 2295, delay: 0, cost: 0}
]},
{label: "Sim58", times: [
	{"label":"Calculate Rating (Sim58)", task: "Calculate Rating", "id":"80fc022d-ba8a-4a30-9db8-0565f976b33b", "starting_time": 2320, "ending_time": 2325, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim58)", task: "Calculate Rating Manually", "id":"8287c909-28be-4090-a60f-c83b2d12a811", "starting_time": 2325, "ending_time": 2330, delay: 0, cost: 0},
	{"label":"Process Rating (Sim58)", task: "Process Rating", "id":"4d3ed267-bea7-446c-ad42-3d9243313fc1", "starting_time": 2330, "ending_time": 2335, delay: 0, cost: 0}
]},
{label: "Sim59", times: [
	{"label":"Calculate Rating (Sim59)", task: "Calculate Rating", "id":"debdad1f-aa08-4a66-b269-67a4bf4ae2c5", "starting_time": 2360, "ending_time": 2365, delay: 0, cost: 0},
	{"label":"Process Rating (Sim59)", task: "Process Rating", "id":"8c927b06-1364-450b-9743-6991567d44e6", "starting_time": 2365, "ending_time": 2370, delay: 0, cost: 0}
]},
{label: "Sim6", times: [
	{"label":"Calculate Rating (Sim6)", task: "Calculate Rating", "id":"2fbccda1-53e7-4848-a4a9-5d9c762caf6e", "starting_time": 240, "ending_time": 245, delay: 0, cost: 0},
	{"label":"Process Rating (Sim6)", task: "Process Rating", "id":"00a9ff66-8138-4566-a1bd-1f9aba58ebb6", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0}
]},
{label: "Sim60", times: [
	{"label":"Calculate Rating (Sim60)", task: "Calculate Rating", "id":"9c9ff04f-5257-4c31-9e40-09163116130b", "starting_time": 2400, "ending_time": 2405, delay: 0, cost: 0},
	{"label":"Process Rating (Sim60)", task: "Process Rating", "id":"71ed4879-c403-43d9-8d84-58811781d1b0", "starting_time": 2405, "ending_time": 2410, delay: 0, cost: 0}
]},
{label: "Sim61", times: [
	{"label":"Calculate Rating (Sim61)", task: "Calculate Rating", "id":"5a366c28-98e1-404e-bda4-81d75ce30086", "starting_time": 2440, "ending_time": 2445, delay: 0, cost: 0},
	{"label":"Process Rating (Sim61)", task: "Process Rating", "id":"9749588f-dce7-4388-bb76-416bc6810a25", "starting_time": 2445, "ending_time": 2450, delay: 0, cost: 0}
]},
{label: "Sim62", times: [
	{"label":"Calculate Rating (Sim62)", task: "Calculate Rating", "id":"05f0f335-e3c7-4095-9a49-68eb550b4061", "starting_time": 2480, "ending_time": 2485, delay: 0, cost: 0},
	{"label":"Process Rating (Sim62)", task: "Process Rating", "id":"0fb0f1bf-14c4-4602-8e1f-b497284a1a45", "starting_time": 2485, "ending_time": 2490, delay: 0, cost: 0}
]},
{label: "Sim63", times: [
	{"label":"Calculate Rating (Sim63)", task: "Calculate Rating", "id":"622a16ee-e483-4dd2-a4f1-ecc4a339f0f7", "starting_time": 2520, "ending_time": 2525, delay: 0, cost: 0},
	{"label":"Process Rating (Sim63)", task: "Process Rating", "id":"17b94251-c3a6-447e-82db-4fa8537bb8f3", "starting_time": 2525, "ending_time": 2530, delay: 0, cost: 0}
]},
{label: "Sim64", times: [
	{"label":"Calculate Rating (Sim64)", task: "Calculate Rating", "id":"63bad6fa-d3f8-427b-b07d-7c6f4ebdbfee", "starting_time": 2560, "ending_time": 2565, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim64)", task: "Calculate Rating Manually", "id":"0e3da255-1dda-4e20-86d1-2c57b2bc3f91", "starting_time": 2565, "ending_time": 2570, delay: 0, cost: 0},
	{"label":"Process Rating (Sim64)", task: "Process Rating", "id":"d2e0b4c7-2c7e-4347-9b95-49d85f6f456b", "starting_time": 2570, "ending_time": 2575, delay: 0, cost: 0}
]},
{label: "Sim65", times: [
	{"label":"Calculate Rating (Sim65)", task: "Calculate Rating", "id":"dd103f28-0ce1-45f9-bdbc-593af13baaac", "starting_time": 2600, "ending_time": 2605, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim65)", task: "Calculate Rating Manually", "id":"45874f69-fef6-4d3b-af67-26485821894a", "starting_time": 2605, "ending_time": 2610, delay: 0, cost: 0},
	{"label":"Process Rating (Sim65)", task: "Process Rating", "id":"12dc8306-6ddd-4e22-8410-65c322e1e822", "starting_time": 2610, "ending_time": 2615, delay: 0, cost: 0}
]},
{label: "Sim66", times: [
	{"label":"Calculate Rating (Sim66)", task: "Calculate Rating", "id":"9889a6e4-66d0-4113-83a9-f2d46371da39", "starting_time": 2640, "ending_time": 2645, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim66)", task: "Calculate Rating Manually", "id":"007773a7-37b0-4b5e-b39c-65fae3237b12", "starting_time": 2645, "ending_time": 2650, delay: 0, cost: 0},
	{"label":"Process Rating (Sim66)", task: "Process Rating", "id":"b063dd7f-a26f-4076-9544-9d2789ff4352", "starting_time": 2650, "ending_time": 2655, delay: 0, cost: 0}
]},
{label: "Sim67", times: [
	{"label":"Calculate Rating (Sim67)", task: "Calculate Rating", "id":"f6e1f9af-800d-479d-ae05-6a8d9172a593", "starting_time": 2680, "ending_time": 2685, delay: 0, cost: 0},
	{"label":"Process Rating (Sim67)", task: "Process Rating", "id":"9aed863f-a222-4869-a0f8-73a1f3bf712d", "starting_time": 2685, "ending_time": 2690, delay: 0, cost: 0}
]},
{label: "Sim68", times: [
	{"label":"Calculate Rating (Sim68)", task: "Calculate Rating", "id":"47ad15e6-c75a-41c9-ad7b-8de161bf42c4", "starting_time": 2720, "ending_time": 2725, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim68)", task: "Calculate Rating Manually", "id":"2ae35e75-50ec-4acf-957d-7e3fef5a3c0d", "starting_time": 2725, "ending_time": 2730, delay: 0, cost: 0},
	{"label":"Process Rating (Sim68)", task: "Process Rating", "id":"0506d990-8cdf-4b4b-b72d-b50688949a7f", "starting_time": 2730, "ending_time": 2735, delay: 0, cost: 0}
]},
{label: "Sim69", times: [
	{"label":"Calculate Rating (Sim69)", task: "Calculate Rating", "id":"64ec0b19-ad8a-4372-b45f-88105ad9fc2a", "starting_time": 2760, "ending_time": 2765, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim69)", task: "Calculate Rating Manually", "id":"45e7978d-a2ef-4f2a-bc5a-f3d0dd78f04b", "starting_time": 2765, "ending_time": 2770, delay: 0, cost: 0},
	{"label":"Process Rating (Sim69)", task: "Process Rating", "id":"309fb201-fdb6-44dd-9233-09cdbd7fc061", "starting_time": 2770, "ending_time": 2775, delay: 0, cost: 0}
]},
{label: "Sim7", times: [
	{"label":"Calculate Rating (Sim7)", task: "Calculate Rating", "id":"939e49b1-17c3-48a0-a10f-4c8cb68c50ef", "starting_time": 280, "ending_time": 285, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim7)", task: "Calculate Rating Manually", "id":"491aa45b-17c6-489d-9035-2ee0e900bbe6", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"Process Rating (Sim7)", task: "Process Rating", "id":"e824765d-c56c-4874-b6fd-8533df089df7", "starting_time": 290, "ending_time": 295, delay: 0, cost: 0}
]},
{label: "Sim70", times: [
	{"label":"Calculate Rating (Sim70)", task: "Calculate Rating", "id":"cca17725-188a-48ce-ab33-a7b1ecd04689", "starting_time": 2800, "ending_time": 2805, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim70)", task: "Calculate Rating Manually", "id":"1d5e4e7b-377a-4872-9ff4-ffb3adfb7ea3", "starting_time": 2805, "ending_time": 2810, delay: 0, cost: 0},
	{"label":"Process Rating (Sim70)", task: "Process Rating", "id":"e993d84b-c2e8-45f1-88a6-bfd32e81eb3e", "starting_time": 2810, "ending_time": 2815, delay: 0, cost: 0}
]},
{label: "Sim71", times: [
	{"label":"Calculate Rating (Sim71)", task: "Calculate Rating", "id":"7c0fe881-13b6-4c26-af6f-6154b5fa2c9c", "starting_time": 2840, "ending_time": 2845, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim71)", task: "Calculate Rating Manually", "id":"33623b0a-1c50-40a2-ad8d-b980cfcf442c", "starting_time": 2845, "ending_time": 2850, delay: 0, cost: 0},
	{"label":"Process Rating (Sim71)", task: "Process Rating", "id":"8d3102d3-039a-487f-b3d3-5608aa0a5313", "starting_time": 2850, "ending_time": 2855, delay: 0, cost: 0}
]},
{label: "Sim72", times: [
	{"label":"Calculate Rating (Sim72)", task: "Calculate Rating", "id":"788466d2-0bc8-4ff2-bc46-715e578dc210", "starting_time": 2880, "ending_time": 2885, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim72)", task: "Calculate Rating Manually", "id":"0f478660-a43e-4d13-9eef-3a109e6115c7", "starting_time": 2885, "ending_time": 2890, delay: 0, cost: 0},
	{"label":"Process Rating (Sim72)", task: "Process Rating", "id":"ccd6a666-cbe4-4956-b9dd-e1dd1c7dff0e", "starting_time": 2890, "ending_time": 2895, delay: 0, cost: 0}
]},
{label: "Sim73", times: [
	{"label":"Calculate Rating (Sim73)", task: "Calculate Rating", "id":"ec7df998-bbae-4e76-bf34-91e1704e5ba7", "starting_time": 2920, "ending_time": 2925, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim73)", task: "Calculate Rating Manually", "id":"38569864-0c72-4319-89fc-fd94c8769232", "starting_time": 2925, "ending_time": 2930, delay: 0, cost: 0},
	{"label":"Process Rating (Sim73)", task: "Process Rating", "id":"0f341345-adec-4857-ba4c-141154fe49f4", "starting_time": 2930, "ending_time": 2935, delay: 0, cost: 0}
]},
{label: "Sim74", times: [
	{"label":"Calculate Rating (Sim74)", task: "Calculate Rating", "id":"a5163f8e-f0e2-4597-9a9e-65deecd1d818", "starting_time": 2960, "ending_time": 2965, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim74)", task: "Calculate Rating Manually", "id":"7d2ea18b-b85d-4e01-81bf-60ddd91c2236", "starting_time": 2965, "ending_time": 2970, delay: 0, cost: 0},
	{"label":"Process Rating (Sim74)", task: "Process Rating", "id":"875edb0e-05b9-469e-9b7b-1e764c195f79", "starting_time": 2970, "ending_time": 2975, delay: 0, cost: 0}
]},
{label: "Sim75", times: [
	{"label":"Calculate Rating (Sim75)", task: "Calculate Rating", "id":"2c74bce0-96ff-4c7c-8f53-7908cca32fc9", "starting_time": 3000, "ending_time": 3005, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim75)", task: "Calculate Rating Manually", "id":"0f105135-5a5e-42f3-bad8-92a21a5c6369", "starting_time": 3005, "ending_time": 3010, delay: 0, cost: 0},
	{"label":"Process Rating (Sim75)", task: "Process Rating", "id":"549f5a16-8407-40e5-b47e-fb3c0e719f29", "starting_time": 3010, "ending_time": 3015, delay: 0, cost: 0}
]},
{label: "Sim76", times: [
	{"label":"Calculate Rating (Sim76)", task: "Calculate Rating", "id":"cfaed5e4-80f8-49a3-9857-174f93a74cf2", "starting_time": 3040, "ending_time": 3045, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim76)", task: "Calculate Rating Manually", "id":"5760c947-8a64-4730-8656-f57dbd7ecf0c", "starting_time": 3045, "ending_time": 3050, delay: 0, cost: 0},
	{"label":"Process Rating (Sim76)", task: "Process Rating", "id":"72d3ebd1-89af-4dd8-8e60-ef4d2741c902", "starting_time": 3050, "ending_time": 3055, delay: 0, cost: 0}
]},
{label: "Sim77", times: [
	{"label":"Calculate Rating (Sim77)", task: "Calculate Rating", "id":"dac4c358-ffb5-49b9-a3b0-30a41f0e9ccf", "starting_time": 3080, "ending_time": 3085, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim77)", task: "Calculate Rating Manually", "id":"0f02b89a-857a-4d09-acd5-9075751e4877", "starting_time": 3085, "ending_time": 3090, delay: 0, cost: 0},
	{"label":"Process Rating (Sim77)", task: "Process Rating", "id":"799a018c-e50f-4da7-84bb-ecf595537cf3", "starting_time": 3090, "ending_time": 3095, delay: 0, cost: 0}
]},
{label: "Sim78", times: [
	{"label":"Calculate Rating (Sim78)", task: "Calculate Rating", "id":"85586341-77eb-41e1-824d-8eb6b148b354", "starting_time": 3120, "ending_time": 3125, delay: 0, cost: 0},
	{"label":"Process Rating (Sim78)", task: "Process Rating", "id":"7b838c9f-1900-4b9c-b80b-56a6a87b82aa", "starting_time": 3125, "ending_time": 3130, delay: 0, cost: 0}
]},
{label: "Sim79", times: [
	{"label":"Calculate Rating (Sim79)", task: "Calculate Rating", "id":"dea461d0-1400-46d9-bc92-7d374b647d00", "starting_time": 3160, "ending_time": 3165, delay: 0, cost: 0},
	{"label":"Process Rating (Sim79)", task: "Process Rating", "id":"090bd9a8-f15a-43db-891e-b90a0f21b621", "starting_time": 3165, "ending_time": 3170, delay: 0, cost: 0}
]},
{label: "Sim8", times: [
	{"label":"Calculate Rating (Sim8)", task: "Calculate Rating", "id":"a726636d-d854-4d2b-b324-fad03adf9ed1", "starting_time": 320, "ending_time": 325, delay: 0, cost: 0},
	{"label":"Process Rating (Sim8)", task: "Process Rating", "id":"880b8f88-5033-413a-aaef-e90243e69226", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0}
]},
{label: "Sim80", times: [
	{"label":"Calculate Rating (Sim80)", task: "Calculate Rating", "id":"6a23bfa5-01a8-4881-a64d-bdcdbbf26333", "starting_time": 3200, "ending_time": 3205, delay: 0, cost: 0},
	{"label":"Process Rating (Sim80)", task: "Process Rating", "id":"ca083d83-713f-47b2-9adc-f513a21e93bf", "starting_time": 3205, "ending_time": 3210, delay: 0, cost: 0}
]},
{label: "Sim81", times: [
	{"label":"Calculate Rating (Sim81)", task: "Calculate Rating", "id":"2b710371-c10c-476f-a0c6-3b795cc24aaf", "starting_time": 3240, "ending_time": 3245, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim81)", task: "Calculate Rating Manually", "id":"ad00cf91-11b2-4590-a7eb-06dc203c797a", "starting_time": 3245, "ending_time": 3250, delay: 0, cost: 0},
	{"label":"Process Rating (Sim81)", task: "Process Rating", "id":"b6a223a3-0d82-49f8-9859-787b7290a615", "starting_time": 3250, "ending_time": 3255, delay: 0, cost: 0}
]},
{label: "Sim82", times: [
	{"label":"Calculate Rating (Sim82)", task: "Calculate Rating", "id":"20d6cc3b-ec8f-458a-8837-9569c64d2b39", "starting_time": 3280, "ending_time": 3285, delay: 0, cost: 0},
	{"label":"Process Rating (Sim82)", task: "Process Rating", "id":"0737544b-fed1-4001-9e17-2f3074b486bf", "starting_time": 3285, "ending_time": 3290, delay: 0, cost: 0}
]},
{label: "Sim83", times: [
	{"label":"Calculate Rating (Sim83)", task: "Calculate Rating", "id":"622b20b2-7f9b-4913-9c53-d052660c6352", "starting_time": 3320, "ending_time": 3325, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim83)", task: "Calculate Rating Manually", "id":"ca2195d8-214d-4e0c-ad7b-2925f7b91756", "starting_time": 3325, "ending_time": 3330, delay: 0, cost: 0},
	{"label":"Process Rating (Sim83)", task: "Process Rating", "id":"1d6cf63c-933e-44c6-b8e1-03637caa017c", "starting_time": 3330, "ending_time": 3335, delay: 0, cost: 0}
]},
{label: "Sim84", times: [
	{"label":"Calculate Rating (Sim84)", task: "Calculate Rating", "id":"10e5c840-258c-4655-9f00-ef56c9651983", "starting_time": 3360, "ending_time": 3365, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim84)", task: "Calculate Rating Manually", "id":"17643dba-d058-40f3-8497-355736361266", "starting_time": 3365, "ending_time": 3370, delay: 0, cost: 0},
	{"label":"Process Rating (Sim84)", task: "Process Rating", "id":"7fcb0225-86e7-4ba1-ae86-1a56118a094d", "starting_time": 3370, "ending_time": 3375, delay: 0, cost: 0}
]},
{label: "Sim85", times: [
	{"label":"Calculate Rating (Sim85)", task: "Calculate Rating", "id":"168639f5-ff1b-4822-b21d-f43f8c3db336", "starting_time": 3400, "ending_time": 3405, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim85)", task: "Calculate Rating Manually", "id":"9104cf5e-7724-4c90-b2e8-2ee5ecd30355", "starting_time": 3405, "ending_time": 3410, delay: 0, cost: 0},
	{"label":"Process Rating (Sim85)", task: "Process Rating", "id":"bc3c1bc5-04ae-461c-827c-a4dc91409993", "starting_time": 3410, "ending_time": 3415, delay: 0, cost: 0}
]},
{label: "Sim86", times: [
	{"label":"Calculate Rating (Sim86)", task: "Calculate Rating", "id":"8109e237-5e71-4316-9676-764608b18b32", "starting_time": 3440, "ending_time": 3445, delay: 0, cost: 0},
	{"label":"Process Rating (Sim86)", task: "Process Rating", "id":"4398e410-8a79-4253-9ee3-6640b5960250", "starting_time": 3445, "ending_time": 3450, delay: 0, cost: 0}
]},
{label: "Sim87", times: [
	{"label":"Calculate Rating (Sim87)", task: "Calculate Rating", "id":"b698b80e-dfd9-419b-aa9c-3a0efe819e3e", "starting_time": 3480, "ending_time": 3485, delay: 0, cost: 0},
	{"label":"Process Rating (Sim87)", task: "Process Rating", "id":"1b445e95-95e1-4d6d-8e12-31af017498b0", "starting_time": 3485, "ending_time": 3490, delay: 0, cost: 0}
]},
{label: "Sim88", times: [
	{"label":"Calculate Rating (Sim88)", task: "Calculate Rating", "id":"519a776e-a4e3-4e17-928e-4c197cd47d18", "starting_time": 3520, "ending_time": 3525, delay: 0, cost: 0},
	{"label":"Process Rating (Sim88)", task: "Process Rating", "id":"66bd5fba-5834-40dc-bf5f-fca9a0c96d9e", "starting_time": 3525, "ending_time": 3530, delay: 0, cost: 0}
]},
{label: "Sim89", times: [
	{"label":"Calculate Rating (Sim89)", task: "Calculate Rating", "id":"474cb738-09dc-49f7-9c28-8482114c837c", "starting_time": 3560, "ending_time": 3565, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim89)", task: "Calculate Rating Manually", "id":"9be137bf-4458-4879-9313-a4775c67ce27", "starting_time": 3565, "ending_time": 3570, delay: 0, cost: 0},
	{"label":"Process Rating (Sim89)", task: "Process Rating", "id":"4223ee6c-6d7b-4129-8b09-7d043674925a", "starting_time": 3570, "ending_time": 3575, delay: 0, cost: 0}
]},
{label: "Sim9", times: [
	{"label":"Calculate Rating (Sim9)", task: "Calculate Rating", "id":"6c933218-cf4d-4ff9-8dcf-a798614171ab", "starting_time": 360, "ending_time": 365, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim9)", task: "Calculate Rating Manually", "id":"6050de2d-ee90-4cbe-be31-454838fe5e12", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"Process Rating (Sim9)", task: "Process Rating", "id":"c88ac256-8bab-4217-a12c-6b0da8eaff5a", "starting_time": 370, "ending_time": 375, delay: 0, cost: 0}
]},
{label: "Sim90", times: [
	{"label":"Calculate Rating (Sim90)", task: "Calculate Rating", "id":"a91e98cb-dbbf-46ae-a835-0546c31d9043", "starting_time": 3600, "ending_time": 3605, delay: 0, cost: 0},
	{"label":"Process Rating (Sim90)", task: "Process Rating", "id":"795d94fc-d8fa-42b1-ad2e-047749074f0f", "starting_time": 3605, "ending_time": 3610, delay: 0, cost: 0}
]},
{label: "Sim91", times: [
	{"label":"Calculate Rating (Sim91)", task: "Calculate Rating", "id":"2f160e02-1f9e-4a8c-9d86-2d618ade144c", "starting_time": 3640, "ending_time": 3645, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim91)", task: "Calculate Rating Manually", "id":"f939be19-42cc-4f7c-9dc5-bbe9553053c7", "starting_time": 3645, "ending_time": 3650, delay: 0, cost: 0},
	{"label":"Process Rating (Sim91)", task: "Process Rating", "id":"eb1a532c-011e-4b09-9894-543a6a81a466", "starting_time": 3650, "ending_time": 3655, delay: 0, cost: 0}
]},
{label: "Sim92", times: [
	{"label":"Calculate Rating (Sim92)", task: "Calculate Rating", "id":"eb864faa-0b3b-4b99-8634-95f9d5cd6e54", "starting_time": 3680, "ending_time": 3685, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim92)", task: "Calculate Rating Manually", "id":"840830c2-6c04-4ce1-95b1-2bf63fef9023", "starting_time": 3685, "ending_time": 3690, delay: 0, cost: 0},
	{"label":"Process Rating (Sim92)", task: "Process Rating", "id":"ee4d34ae-64ae-4040-b542-1f6b0180cb8c", "starting_time": 3690, "ending_time": 3695, delay: 0, cost: 0}
]},
{label: "Sim93", times: [
	{"label":"Calculate Rating (Sim93)", task: "Calculate Rating", "id":"e6b4895e-67e8-48c9-aa42-e50492c7bdc8", "starting_time": 3720, "ending_time": 3725, delay: 0, cost: 0},
	{"label":"Process Rating (Sim93)", task: "Process Rating", "id":"605c815b-4c0d-4784-8314-7396ecb0413d", "starting_time": 3725, "ending_time": 3730, delay: 0, cost: 0}
]},
{label: "Sim94", times: [
	{"label":"Calculate Rating (Sim94)", task: "Calculate Rating", "id":"659f5929-885b-4001-8e26-ac1283c7ffb0", "starting_time": 3760, "ending_time": 3765, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim94)", task: "Calculate Rating Manually", "id":"42232b75-70ed-45cd-b9d9-0755a80e988c", "starting_time": 3765, "ending_time": 3770, delay: 0, cost: 0},
	{"label":"Process Rating (Sim94)", task: "Process Rating", "id":"526aa4ee-315e-44d8-aa3e-a4304ea7c58d", "starting_time": 3770, "ending_time": 3775, delay: 0, cost: 0}
]},
{label: "Sim95", times: [
	{"label":"Calculate Rating (Sim95)", task: "Calculate Rating", "id":"c1df70e3-df67-4d1d-a448-3c1d0a440f21", "starting_time": 3800, "ending_time": 3805, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim95)", task: "Calculate Rating Manually", "id":"9c3e13c2-63fb-43f3-a2d4-af80b60a7ef9", "starting_time": 3805, "ending_time": 3810, delay: 0, cost: 0},
	{"label":"Process Rating (Sim95)", task: "Process Rating", "id":"6b596eae-6756-4eeb-b6c5-6247a056c364", "starting_time": 3810, "ending_time": 3815, delay: 0, cost: 0}
]},
{label: "Sim96", times: [
	{"label":"Calculate Rating (Sim96)", task: "Calculate Rating", "id":"4fa8fdaa-eb50-4748-bcac-7ae306e297c4", "starting_time": 3840, "ending_time": 3845, delay: 0, cost: 0},
	{"label":"Process Rating (Sim96)", task: "Process Rating", "id":"d8c63a69-3150-498b-bc33-35911169cf5f", "starting_time": 3845, "ending_time": 3850, delay: 0, cost: 0}
]},
{label: "Sim97", times: [
	{"label":"Calculate Rating (Sim97)", task: "Calculate Rating", "id":"0a0b566b-be74-4ff0-9de8-b526437f7bf3", "starting_time": 3880, "ending_time": 3885, delay: 0, cost: 0},
	{"label":"Process Rating (Sim97)", task: "Process Rating", "id":"e344c1d2-0124-4ad0-85c5-6f718fe36dc6", "starting_time": 3885, "ending_time": 3890, delay: 0, cost: 0}
]},
{label: "Sim98", times: [
	{"label":"Calculate Rating (Sim98)", task: "Calculate Rating", "id":"21b1885e-f13e-48af-8a35-ae93488d650a", "starting_time": 3920, "ending_time": 3925, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim98)", task: "Calculate Rating Manually", "id":"44aae2a7-a625-44cf-b006-0f3207763638", "starting_time": 3925, "ending_time": 3930, delay: 0, cost: 0},
	{"label":"Process Rating (Sim98)", task: "Process Rating", "id":"1f84860b-54f8-4d45-b7c0-3ed25e5dc655", "starting_time": 3930, "ending_time": 3935, delay: 0, cost: 0}
]},
{label: "Sim99", times: [
	{"label":"Calculate Rating (Sim99)", task: "Calculate Rating", "id":"97982a07-9004-4c59-973c-fe291ece5ec5", "starting_time": 3960, "ending_time": 3965, delay: 0, cost: 0},
	{"label":"Calculate Rating Manually (Sim99)", task: "Calculate Rating Manually", "id":"26581c48-d895-4fa1-9fd5-a1e86a7a62c1", "starting_time": 3965, "ending_time": 3970, delay: 0, cost: 0},
	{"label":"Process Rating (Sim99)", task: "Process Rating", "id":"6cdc7b5b-25a9-401b-9213-820d070d772c", "starting_time": 3970, "ending_time": 3975, delay: 0, cost: 0}
]},
];
