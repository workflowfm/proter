var tasks = [
	"Script Task",
	"Service Task",
	"User Task A",
	"User Task B",
];

var resourceData = [
{label: "r1", times: [
	{"label":"Service Task (Sim0)", task: "Service Task", "id":"9aeefe97-d1bc-4b0e-bbf1-567bd345703c", "starting_time": 0, "ending_time": 5, delay: 0, cost: 0},
	{"label":"User Task B (Sim0)", task: "User Task B", "id":"a972af7e-dbcb-4c7e-b536-6975e9ec1ca6", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"User Task A (Sim0)", task: "User Task A", "id":"0f22b0fc-5c85-4d76-a1d4-d72672033e63", "starting_time": 10, "ending_time": 15, delay: 5, cost: 0},
	{"label":"Script Task (Sim0)", task: "Script Task", "id":"a907b05c-4141-4485-b4d0-1c2098265fcf", "starting_time": 15, "ending_time": 20, delay: 0, cost: 0},
	{"label":"Service Task (Sim1)", task: "Service Task", "id":"b73c83d4-179f-4851-aeaf-94dffab28a74", "starting_time": 20, "ending_time": 25, delay: 0, cost: 0},
	{"label":"User Task B (Sim1)", task: "User Task B", "id":"8e138599-bf7f-407a-8aa4-29fb8422cea4", "starting_time": 25, "ending_time": 30, delay: 0, cost: 0},
	{"label":"User Task A (Sim1)", task: "User Task A", "id":"3482bfe0-a45b-42a4-b252-53cd8ac643a1", "starting_time": 30, "ending_time": 35, delay: 5, cost: 0},
	{"label":"Script Task (Sim1)", task: "Script Task", "id":"ecd043a9-5472-4000-ae9e-68ca4a706021", "starting_time": 35, "ending_time": 40, delay: 0, cost: 0},
	{"label":"Service Task (Sim2)", task: "Service Task", "id":"d1751d07-60d3-4d98-bc44-0f37537fd8c1", "starting_time": 40, "ending_time": 45, delay: 0, cost: 0},
	{"label":"User Task A (Sim2)", task: "User Task A", "id":"b3009dd3-05a4-4a62-aef8-7c39bb4fe847", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"User Task B (Sim2)", task: "User Task B", "id":"b48d9986-fb00-460f-b117-a7821d6e5532", "starting_time": 50, "ending_time": 55, delay: 5, cost: 0},
	{"label":"Script Task (Sim2)", task: "Script Task", "id":"169c8506-8c9e-42f2-828d-6cbe3abf9321", "starting_time": 55, "ending_time": 60, delay: 0, cost: 0},
	{"label":"Service Task (Sim3)", task: "Service Task", "id":"a88b59b4-64c8-46c3-ad43-964049d5bbef", "starting_time": 60, "ending_time": 65, delay: 0, cost: 0},
	{"label":"User Task B (Sim3)", task: "User Task B", "id":"e411de9a-6315-457b-bec6-003194db937c", "starting_time": 65, "ending_time": 70, delay: 0, cost: 0},
	{"label":"User Task A (Sim3)", task: "User Task A", "id":"e5cb542d-e35e-4356-b81c-5837038a6eac", "starting_time": 70, "ending_time": 75, delay: 5, cost: 0},
	{"label":"Script Task (Sim3)", task: "Script Task", "id":"c07116c8-8fd6-4191-a380-87f55abc269b", "starting_time": 75, "ending_time": 80, delay: 0, cost: 0},
	{"label":"Service Task (Sim4)", task: "Service Task", "id":"2128bdd0-6abb-4b1a-a8ba-537ed08d3bcd", "starting_time": 80, "ending_time": 85, delay: 0, cost: 0},
	{"label":"User Task B (Sim4)", task: "User Task B", "id":"862f8664-ca10-40a4-a742-2be7c6b6bb7b", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"User Task A (Sim4)", task: "User Task A", "id":"2b0d5c0c-efe0-4907-a17e-84956e1325fc", "starting_time": 90, "ending_time": 95, delay: 5, cost: 0},
	{"label":"Script Task (Sim4)", task: "Script Task", "id":"3745472b-59bf-477d-9f9f-d6884fcde0b4", "starting_time": 95, "ending_time": 100, delay: 0, cost: 0},
	{"label":"Service Task (Sim5)", task: "Service Task", "id":"c5d1174e-6674-4e34-a2c9-5a6c15935c6e", "starting_time": 100, "ending_time": 105, delay: 0, cost: 0},
	{"label":"User Task B (Sim5)", task: "User Task B", "id":"fdd3a626-6b79-4481-899f-40629c7756b7", "starting_time": 105, "ending_time": 110, delay: 0, cost: 0},
	{"label":"User Task A (Sim5)", task: "User Task A", "id":"fff118dd-5b26-447f-b600-8186a5ba6a9f", "starting_time": 110, "ending_time": 115, delay: 5, cost: 0},
	{"label":"Script Task (Sim5)", task: "Script Task", "id":"50517f86-870d-49ba-a2d5-4e63dd5d50c2", "starting_time": 115, "ending_time": 120, delay: 0, cost: 0},
	{"label":"Service Task (Sim6)", task: "Service Task", "id":"201fcbe3-bbb3-437b-8323-b4055c71f97d", "starting_time": 120, "ending_time": 125, delay: 0, cost: 0},
	{"label":"User Task B (Sim6)", task: "User Task B", "id":"d0846c1f-0618-4fda-b527-290891ac9215", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"User Task A (Sim6)", task: "User Task A", "id":"f8833937-deac-4390-b870-f29778c537a1", "starting_time": 130, "ending_time": 135, delay: 5, cost: 0},
	{"label":"Script Task (Sim6)", task: "Script Task", "id":"fdb526bb-a7e3-4ab4-9f04-458678d38cf1", "starting_time": 135, "ending_time": 140, delay: 0, cost: 0},
	{"label":"Service Task (Sim7)", task: "Service Task", "id":"0b0b80ed-e2e3-4b5f-9aa1-ea4dbedee664", "starting_time": 140, "ending_time": 145, delay: 0, cost: 0},
	{"label":"User Task A (Sim7)", task: "User Task A", "id":"382fb91e-ae5c-47ab-a4fe-8c54d3a82f5d", "starting_time": 145, "ending_time": 150, delay: 0, cost: 0},
	{"label":"User Task B (Sim7)", task: "User Task B", "id":"642048f7-4ce4-4e9d-a502-a503085b1aa2", "starting_time": 150, "ending_time": 155, delay: 5, cost: 0},
	{"label":"Script Task (Sim7)", task: "Script Task", "id":"6362da26-64c1-4135-9923-a51a9ef551e3", "starting_time": 155, "ending_time": 160, delay: 0, cost: 0},
	{"label":"Service Task (Sim8)", task: "Service Task", "id":"ba8a6053-ea27-49d9-80eb-4f96eb8d6aac", "starting_time": 160, "ending_time": 165, delay: 0, cost: 0},
	{"label":"User Task A (Sim8)", task: "User Task A", "id":"eca94382-c539-4091-bee6-5c3c752e2731", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"User Task B (Sim8)", task: "User Task B", "id":"0cb6d8e5-0f81-46cf-bb89-9ca3a18fe5dd", "starting_time": 170, "ending_time": 175, delay: 5, cost: 0},
	{"label":"Script Task (Sim8)", task: "Script Task", "id":"086f487f-fdcb-4873-a48b-63f18b072c3d", "starting_time": 175, "ending_time": 180, delay: 0, cost: 0},
	{"label":"Service Task (Sim9)", task: "Service Task", "id":"80d09e6f-4418-4d44-b160-5236090fd8c7", "starting_time": 180, "ending_time": 185, delay: 0, cost: 0},
	{"label":"User Task A (Sim9)", task: "User Task A", "id":"d9f66662-d5dc-4df8-8933-303ad6f64e30", "starting_time": 185, "ending_time": 190, delay: 0, cost: 0},
	{"label":"User Task B (Sim9)", task: "User Task B", "id":"e94ad542-80c3-48f4-aa81-87428f9e3531", "starting_time": 190, "ending_time": 195, delay: 5, cost: 0},
	{"label":"Script Task (Sim9)", task: "Script Task", "id":"d799a951-a514-4da6-8853-46b34efc3ab8", "starting_time": 195, "ending_time": 200, delay: 0, cost: 0},
	{"label":"Service Task (Sim10)", task: "Service Task", "id":"d08e8b81-4a32-4f82-8ada-5406defce3c3", "starting_time": 200, "ending_time": 205, delay: 0, cost: 0},
	{"label":"User Task B (Sim10)", task: "User Task B", "id":"bc25ae4c-3e55-401d-9359-735afee19f37", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"User Task A (Sim10)", task: "User Task A", "id":"3025e3ba-719e-4290-9eaf-00f5344e6eda", "starting_time": 210, "ending_time": 215, delay: 5, cost: 0},
	{"label":"Script Task (Sim10)", task: "Script Task", "id":"48f39070-9d85-4af5-a5a6-3aeb605c1d3a", "starting_time": 215, "ending_time": 220, delay: 0, cost: 0},
	{"label":"Service Task (Sim11)", task: "Service Task", "id":"c4b3a213-07b1-42ce-b472-34b599896a45", "starting_time": 220, "ending_time": 225, delay: 0, cost: 0},
	{"label":"User Task A (Sim11)", task: "User Task A", "id":"34305813-c57a-4604-82d1-d6b975c6d270", "starting_time": 225, "ending_time": 230, delay: 0, cost: 0},
	{"label":"User Task B (Sim11)", task: "User Task B", "id":"6b7eaa0c-5961-481e-94a0-340c94c23078", "starting_time": 230, "ending_time": 235, delay: 5, cost: 0},
	{"label":"Script Task (Sim11)", task: "Script Task", "id":"92be2c6d-25b0-46c1-8d3f-f90877b29fb1", "starting_time": 235, "ending_time": 240, delay: 0, cost: 0},
	{"label":"Service Task (Sim12)", task: "Service Task", "id":"2675c2ef-a48e-4139-8203-916114634e8f", "starting_time": 240, "ending_time": 245, delay: 0, cost: 0},
	{"label":"User Task B (Sim12)", task: "User Task B", "id":"5cf2dcef-3eea-47b7-b8e9-e1f2a088b5c0", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"User Task A (Sim12)", task: "User Task A", "id":"7f0db2da-37de-452a-9f82-5623bd399f58", "starting_time": 250, "ending_time": 255, delay: 5, cost: 0},
	{"label":"Script Task (Sim12)", task: "Script Task", "id":"2f65d76f-8713-48a3-b1c6-2fb612e247e6", "starting_time": 255, "ending_time": 260, delay: 0, cost: 0},
	{"label":"Service Task (Sim13)", task: "Service Task", "id":"3ab48890-0317-4bf1-9bc0-200e599b25bd", "starting_time": 260, "ending_time": 265, delay: 0, cost: 0},
	{"label":"User Task A (Sim13)", task: "User Task A", "id":"844fcb77-153b-4069-a539-dd59b530ffc4", "starting_time": 265, "ending_time": 270, delay: 0, cost: 0},
	{"label":"User Task B (Sim13)", task: "User Task B", "id":"1a21263c-8586-454d-bbce-041203f93f9e", "starting_time": 270, "ending_time": 275, delay: 5, cost: 0},
	{"label":"Script Task (Sim13)", task: "Script Task", "id":"dd1e6b0b-6cd6-4895-96b6-1a91c79c7e8e", "starting_time": 275, "ending_time": 280, delay: 0, cost: 0},
	{"label":"Service Task (Sim14)", task: "Service Task", "id":"5bfe559a-a16d-4b49-8e7d-c5527a963091", "starting_time": 280, "ending_time": 285, delay: 0, cost: 0},
	{"label":"User Task B (Sim14)", task: "User Task B", "id":"e3cfc3ef-5e63-463d-ad4e-c0356c6f1d8c", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"User Task A (Sim14)", task: "User Task A", "id":"32933fca-ef9c-41a0-a0e3-c5b85ae35aea", "starting_time": 290, "ending_time": 295, delay: 5, cost: 0},
	{"label":"Script Task (Sim14)", task: "Script Task", "id":"e5438261-3be7-4aa1-aae2-c718fb2dd3fe", "starting_time": 295, "ending_time": 300, delay: 0, cost: 0},
	{"label":"Service Task (Sim15)", task: "Service Task", "id":"adc7f2e9-423f-48d3-9529-c06dc9ca7eb8", "starting_time": 300, "ending_time": 305, delay: 0, cost: 0},
	{"label":"User Task A (Sim15)", task: "User Task A", "id":"16ead85d-1ed6-4c65-8f98-307bfbc3f3c0", "starting_time": 305, "ending_time": 310, delay: 0, cost: 0},
	{"label":"User Task B (Sim15)", task: "User Task B", "id":"2f287060-8b58-4417-8396-eefdcfd38d0e", "starting_time": 310, "ending_time": 315, delay: 5, cost: 0},
	{"label":"Script Task (Sim15)", task: "Script Task", "id":"435008b3-2d0b-49ea-a3eb-83f6663ad225", "starting_time": 315, "ending_time": 320, delay: 0, cost: 0},
	{"label":"Service Task (Sim16)", task: "Service Task", "id":"aeed69ed-8dab-400c-a760-95f03b72d233", "starting_time": 320, "ending_time": 325, delay: 0, cost: 0},
	{"label":"User Task B (Sim16)", task: "User Task B", "id":"e693db44-11f9-4213-a380-5fb5eeff74e6", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"User Task A (Sim16)", task: "User Task A", "id":"7aadc2fd-5a70-483d-9328-4c315b6630d4", "starting_time": 330, "ending_time": 335, delay: 5, cost: 0},
	{"label":"Script Task (Sim16)", task: "Script Task", "id":"a083b002-f038-4851-a695-c497213eb449", "starting_time": 335, "ending_time": 340, delay: 0, cost: 0},
	{"label":"Service Task (Sim17)", task: "Service Task", "id":"afb7851d-a472-4b45-b03b-e9e7d3f0f444", "starting_time": 340, "ending_time": 345, delay: 0, cost: 0},
	{"label":"User Task B (Sim17)", task: "User Task B", "id":"f1e2b102-9262-48e1-80e9-6182d2ce5129", "starting_time": 345, "ending_time": 350, delay: 0, cost: 0},
	{"label":"User Task A (Sim17)", task: "User Task A", "id":"7903a7c0-8c26-4305-ae70-daa3da56d38e", "starting_time": 350, "ending_time": 355, delay: 5, cost: 0},
	{"label":"Script Task (Sim17)", task: "Script Task", "id":"470a5c67-297e-4447-8bde-3f80d3d76087", "starting_time": 355, "ending_time": 360, delay: 0, cost: 0},
	{"label":"Service Task (Sim18)", task: "Service Task", "id":"8c1bf011-c443-48ec-bdba-67e939b73d09", "starting_time": 360, "ending_time": 365, delay: 0, cost: 0},
	{"label":"User Task B (Sim18)", task: "User Task B", "id":"cdadd5a5-0039-4de6-8201-74d6edcf937a", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"User Task A (Sim18)", task: "User Task A", "id":"54952bc3-5027-468b-9ac7-1909afae51b0", "starting_time": 370, "ending_time": 375, delay: 5, cost: 0},
	{"label":"Script Task (Sim18)", task: "Script Task", "id":"4489d7a9-6689-47c7-8985-01f104149fcb", "starting_time": 375, "ending_time": 380, delay: 0, cost: 0},
	{"label":"Service Task (Sim19)", task: "Service Task", "id":"51f58c36-983d-471a-9287-db2d9213b19f", "starting_time": 380, "ending_time": 385, delay: 0, cost: 0},
	{"label":"User Task B (Sim19)", task: "User Task B", "id":"a6f30bb6-1bbe-4cb6-a7ca-b1a79b2f3a5a", "starting_time": 385, "ending_time": 390, delay: 0, cost: 0},
	{"label":"User Task A (Sim19)", task: "User Task A", "id":"f569f45f-b922-469d-91b4-8f5f18662503", "starting_time": 390, "ending_time": 395, delay: 5, cost: 0},
	{"label":"Script Task (Sim19)", task: "Script Task", "id":"1b4ad041-939c-4766-8288-5cdd3695e9c9", "starting_time": 395, "ending_time": 400, delay: 0, cost: 0},
	{"label":"Service Task (Sim20)", task: "Service Task", "id":"29dd7e6b-72f4-4c5e-93a0-18d929042cc7", "starting_time": 400, "ending_time": 405, delay: 0, cost: 0},
	{"label":"User Task B (Sim20)", task: "User Task B", "id":"b72a2849-50a7-4b00-89e2-78ffc42c02b5", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"User Task A (Sim20)", task: "User Task A", "id":"fe4b0a3d-1125-49fb-af2d-e7e49beaafc2", "starting_time": 410, "ending_time": 415, delay: 5, cost: 0},
	{"label":"Script Task (Sim20)", task: "Script Task", "id":"51ccdac9-f76e-4d51-85c9-21dd6453cfc1", "starting_time": 415, "ending_time": 420, delay: 0, cost: 0},
	{"label":"Service Task (Sim21)", task: "Service Task", "id":"07cf7f14-0f5e-4390-a132-a5cbd9f82c20", "starting_time": 420, "ending_time": 425, delay: 0, cost: 0},
	{"label":"User Task B (Sim21)", task: "User Task B", "id":"f597b297-c037-4534-b8ba-e63bc2736fcf", "starting_time": 425, "ending_time": 430, delay: 0, cost: 0},
	{"label":"User Task A (Sim21)", task: "User Task A", "id":"024f96ef-8630-4ef2-97fd-ad0816b06dc2", "starting_time": 430, "ending_time": 435, delay: 5, cost: 0},
	{"label":"Script Task (Sim21)", task: "Script Task", "id":"e8fad3c6-468c-488a-97cc-95113b5c4b48", "starting_time": 435, "ending_time": 440, delay: 0, cost: 0},
	{"label":"Service Task (Sim22)", task: "Service Task", "id":"a1b91eb6-7292-4e45-9ffb-6c330fe69b88", "starting_time": 440, "ending_time": 445, delay: 0, cost: 0},
	{"label":"User Task A (Sim22)", task: "User Task A", "id":"f1803885-62c2-4701-9583-3684cb547b89", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"User Task B (Sim22)", task: "User Task B", "id":"79f96d3b-7f2d-406a-9014-cca437611588", "starting_time": 450, "ending_time": 455, delay: 5, cost: 0},
	{"label":"Script Task (Sim22)", task: "Script Task", "id":"b0c45086-1b7f-46a5-a6f9-7bcbd3fc8c92", "starting_time": 455, "ending_time": 460, delay: 0, cost: 0},
	{"label":"Service Task (Sim23)", task: "Service Task", "id":"9e54bd85-ce87-4428-a330-67a32df72f09", "starting_time": 460, "ending_time": 465, delay: 0, cost: 0},
	{"label":"User Task B (Sim23)", task: "User Task B", "id":"a56ba3f2-b2e3-48e3-bf4a-cd3c3bb4f008", "starting_time": 465, "ending_time": 470, delay: 0, cost: 0},
	{"label":"User Task A (Sim23)", task: "User Task A", "id":"5187ab08-66cf-41f2-a09e-555a0e8af82c", "starting_time": 470, "ending_time": 475, delay: 5, cost: 0},
	{"label":"Script Task (Sim23)", task: "Script Task", "id":"f44e1666-0251-4296-a361-053b70b1acbe", "starting_time": 475, "ending_time": 480, delay: 0, cost: 0},
	{"label":"Service Task (Sim24)", task: "Service Task", "id":"cac27fb0-86e7-4244-a37f-c08346556d95", "starting_time": 480, "ending_time": 485, delay: 0, cost: 0},
	{"label":"User Task B (Sim24)", task: "User Task B", "id":"d035e918-46cc-48c0-8f98-2c36ee410c46", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"User Task A (Sim24)", task: "User Task A", "id":"6c504f44-27b2-42f6-ba4c-67a0a46719d1", "starting_time": 490, "ending_time": 495, delay: 5, cost: 0},
	{"label":"Script Task (Sim24)", task: "Script Task", "id":"1cfa9f1b-af26-4265-b141-469352aeb806", "starting_time": 495, "ending_time": 500, delay: 0, cost: 0},
	{"label":"Service Task (Sim25)", task: "Service Task", "id":"408834e9-9651-44f8-9561-3835b864fa85", "starting_time": 500, "ending_time": 505, delay: 0, cost: 0},
	{"label":"User Task A (Sim25)", task: "User Task A", "id":"c721b774-0ce1-4ab4-bc31-e25e7b921a5c", "starting_time": 505, "ending_time": 510, delay: 0, cost: 0},
	{"label":"User Task B (Sim25)", task: "User Task B", "id":"6168a45a-05a2-40de-9244-4ecdd03ea6ec", "starting_time": 510, "ending_time": 515, delay: 5, cost: 0},
	{"label":"Script Task (Sim25)", task: "Script Task", "id":"54323aad-8246-4463-b59b-f5d31299a929", "starting_time": 515, "ending_time": 520, delay: 0, cost: 0},
	{"label":"Service Task (Sim26)", task: "Service Task", "id":"880aad7d-1c8d-4e2e-aff7-3c841ee2a0a9", "starting_time": 520, "ending_time": 525, delay: 0, cost: 0},
	{"label":"User Task A (Sim26)", task: "User Task A", "id":"47cff48f-beda-4a6a-95dc-c1343f9bd8d4", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"User Task B (Sim26)", task: "User Task B", "id":"556150cf-096b-41e7-93ad-7352b96259bb", "starting_time": 530, "ending_time": 535, delay: 5, cost: 0},
	{"label":"Script Task (Sim26)", task: "Script Task", "id":"0cc0e1fe-4f33-4623-b893-af30dee0b06c", "starting_time": 535, "ending_time": 540, delay: 0, cost: 0},
	{"label":"Service Task (Sim27)", task: "Service Task", "id":"b9d612d4-6d78-4ac9-ad81-f6f8850349e7", "starting_time": 540, "ending_time": 545, delay: 0, cost: 0},
	{"label":"User Task B (Sim27)", task: "User Task B", "id":"bee4a5a5-5877-46d5-bf6a-b341429747c6", "starting_time": 545, "ending_time": 550, delay: 0, cost: 0},
	{"label":"User Task A (Sim27)", task: "User Task A", "id":"32dd742a-cdb6-4184-995e-6f744c40040b", "starting_time": 550, "ending_time": 555, delay: 5, cost: 0},
	{"label":"Script Task (Sim27)", task: "Script Task", "id":"17cd5b98-f49a-488d-8a43-e315efc2cd9f", "starting_time": 555, "ending_time": 560, delay: 0, cost: 0},
	{"label":"Service Task (Sim28)", task: "Service Task", "id":"d34003e5-c6ab-4c67-9b1c-ef58f0fbcb41", "starting_time": 560, "ending_time": 565, delay: 0, cost: 0},
	{"label":"User Task A (Sim28)", task: "User Task A", "id":"bd78b4da-721f-4de9-8516-0e6ba4f50572", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"User Task B (Sim28)", task: "User Task B", "id":"c233458b-75bd-44ba-9eee-edd3ad98221c", "starting_time": 570, "ending_time": 575, delay: 5, cost: 0},
	{"label":"Script Task (Sim28)", task: "Script Task", "id":"df40f3d2-ff0d-49c6-aefd-11e23c9edcfa", "starting_time": 575, "ending_time": 580, delay: 0, cost: 0},
	{"label":"Service Task (Sim29)", task: "Service Task", "id":"b63940e9-732d-4acc-9191-1517f3677906", "starting_time": 580, "ending_time": 585, delay: 0, cost: 0},
	{"label":"User Task B (Sim29)", task: "User Task B", "id":"ac4677ad-bed2-4004-bd34-af390a5dc08d", "starting_time": 585, "ending_time": 590, delay: 0, cost: 0},
	{"label":"User Task A (Sim29)", task: "User Task A", "id":"5d696e5e-5a19-4615-85b4-73893e12b22f", "starting_time": 590, "ending_time": 595, delay: 5, cost: 0},
	{"label":"Script Task (Sim29)", task: "Script Task", "id":"a6e3efdb-09a4-4830-98af-860871b8cbba", "starting_time": 595, "ending_time": 600, delay: 0, cost: 0},
	{"label":"Service Task (Sim30)", task: "Service Task", "id":"88e742b8-fac6-429e-8852-67bcf4ad480d", "starting_time": 600, "ending_time": 605, delay: 0, cost: 0},
	{"label":"User Task B (Sim30)", task: "User Task B", "id":"475ce0bd-a6ae-4b21-96a4-337c9eacdedc", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"User Task A (Sim30)", task: "User Task A", "id":"62b19b48-2cc7-463c-ae6b-dcc27e809097", "starting_time": 610, "ending_time": 615, delay: 5, cost: 0},
	{"label":"Script Task (Sim30)", task: "Script Task", "id":"d7983d73-6ca4-4605-ab41-61c005278956", "starting_time": 615, "ending_time": 620, delay: 0, cost: 0},
	{"label":"Service Task (Sim31)", task: "Service Task", "id":"54dc27d1-3259-41f1-99d9-c6be53f24d9f", "starting_time": 620, "ending_time": 625, delay: 0, cost: 0},
	{"label":"User Task B (Sim31)", task: "User Task B", "id":"a1ecf88c-2f0a-4274-81f5-4cfeb5ea5df1", "starting_time": 625, "ending_time": 630, delay: 0, cost: 0},
	{"label":"User Task A (Sim31)", task: "User Task A", "id":"ee63f6ae-21e6-4c6e-91e5-738fc9df15b6", "starting_time": 630, "ending_time": 635, delay: 5, cost: 0},
	{"label":"Script Task (Sim31)", task: "Script Task", "id":"fb9ea121-05dc-4a7e-a40a-849167df5ddf", "starting_time": 635, "ending_time": 640, delay: 0, cost: 0},
	{"label":"Service Task (Sim32)", task: "Service Task", "id":"2e53bf21-f606-4daf-b6a6-9d88cbfea503", "starting_time": 640, "ending_time": 645, delay: 0, cost: 0},
	{"label":"User Task B (Sim32)", task: "User Task B", "id":"8c13151a-4ebb-43eb-ae99-9fd1eef38467", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"User Task A (Sim32)", task: "User Task A", "id":"bd66f9c5-527f-40f3-a4c5-8ce0a67ab3f6", "starting_time": 650, "ending_time": 655, delay: 5, cost: 0},
	{"label":"Script Task (Sim32)", task: "Script Task", "id":"9158c8ae-8ce9-467d-9507-52c2e44f324d", "starting_time": 655, "ending_time": 660, delay: 0, cost: 0},
	{"label":"Service Task (Sim33)", task: "Service Task", "id":"50be62c5-dfb7-45a7-98ef-cc87ce09abec", "starting_time": 660, "ending_time": 665, delay: 0, cost: 0},
	{"label":"User Task A (Sim33)", task: "User Task A", "id":"836cb8dc-dfd8-43cd-80c1-40d86a18bd5f", "starting_time": 665, "ending_time": 670, delay: 0, cost: 0},
	{"label":"User Task B (Sim33)", task: "User Task B", "id":"861257f0-1549-4fc2-868b-da77527399c5", "starting_time": 670, "ending_time": 675, delay: 5, cost: 0},
	{"label":"Script Task (Sim33)", task: "Script Task", "id":"c4efef07-5f64-4a66-b908-82c0d81152fc", "starting_time": 675, "ending_time": 680, delay: 0, cost: 0},
	{"label":"Service Task (Sim34)", task: "Service Task", "id":"66734c21-27df-481f-ac8c-f53fa6188927", "starting_time": 680, "ending_time": 685, delay: 0, cost: 0},
	{"label":"User Task A (Sim34)", task: "User Task A", "id":"964578c9-adab-4a67-828b-deec67e66912", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"User Task B (Sim34)", task: "User Task B", "id":"ac39e52c-5b27-40c5-8854-0e77a0930373", "starting_time": 690, "ending_time": 695, delay: 5, cost: 0},
	{"label":"Script Task (Sim34)", task: "Script Task", "id":"9a2a03b8-36ec-40a3-a827-70641e878772", "starting_time": 695, "ending_time": 700, delay: 0, cost: 0},
	{"label":"Service Task (Sim35)", task: "Service Task", "id":"40f22aff-c57b-4e51-a8ff-4de4a68289ee", "starting_time": 700, "ending_time": 705, delay: 0, cost: 0},
	{"label":"User Task B (Sim35)", task: "User Task B", "id":"a2dec0df-e745-45f0-adbc-e710a8d00b8f", "starting_time": 705, "ending_time": 710, delay: 0, cost: 0},
	{"label":"User Task A (Sim35)", task: "User Task A", "id":"7c02c57c-5311-4959-a547-9e46f6af931d", "starting_time": 710, "ending_time": 715, delay: 5, cost: 0},
	{"label":"Script Task (Sim35)", task: "Script Task", "id":"b19d3e3c-69d4-45d3-a275-344656461556", "starting_time": 715, "ending_time": 720, delay: 0, cost: 0},
	{"label":"Service Task (Sim36)", task: "Service Task", "id":"4c952b9c-09c0-4bc8-9ede-8c8ecc7d6da9", "starting_time": 720, "ending_time": 725, delay: 0, cost: 0},
	{"label":"User Task B (Sim36)", task: "User Task B", "id":"a471bc2d-e06e-44f7-ae92-c779dc622e55", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"User Task A (Sim36)", task: "User Task A", "id":"c321530c-3693-4366-9b44-f280e3b8919a", "starting_time": 730, "ending_time": 735, delay: 5, cost: 0},
	{"label":"Script Task (Sim36)", task: "Script Task", "id":"9e70c758-1346-419c-871c-76fb6a989877", "starting_time": 735, "ending_time": 740, delay: 0, cost: 0},
	{"label":"Service Task (Sim37)", task: "Service Task", "id":"a7d3c64c-224e-441a-ac9e-7f8942ad9a77", "starting_time": 740, "ending_time": 745, delay: 0, cost: 0},
	{"label":"User Task A (Sim37)", task: "User Task A", "id":"e9c6624c-da9a-4317-b36c-15ea65e53e82", "starting_time": 745, "ending_time": 750, delay: 0, cost: 0},
	{"label":"User Task B (Sim37)", task: "User Task B", "id":"1334ce7e-cddb-456e-a3a4-d5f61148d751", "starting_time": 750, "ending_time": 755, delay: 5, cost: 0},
	{"label":"Script Task (Sim37)", task: "Script Task", "id":"b48212e3-2f3c-45ef-87b5-fc1f2b41a46b", "starting_time": 755, "ending_time": 760, delay: 0, cost: 0},
	{"label":"Service Task (Sim38)", task: "Service Task", "id":"8e332ee7-cb68-4a44-9b7c-2829cfb640ef", "starting_time": 760, "ending_time": 765, delay: 0, cost: 0},
	{"label":"User Task A (Sim38)", task: "User Task A", "id":"ec44f6a4-fc93-47a8-af6c-85bc9ad04c14", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"User Task B (Sim38)", task: "User Task B", "id":"367dd570-64f1-486e-8aff-36ba81b8ee44", "starting_time": 770, "ending_time": 775, delay: 5, cost: 0},
	{"label":"Script Task (Sim38)", task: "Script Task", "id":"b3b3a414-2f10-4c87-9edb-a1b18161a6bf", "starting_time": 775, "ending_time": 780, delay: 0, cost: 0},
	{"label":"Service Task (Sim39)", task: "Service Task", "id":"02f2a21d-1a91-4425-aea3-ae211f6e6847", "starting_time": 780, "ending_time": 785, delay: 0, cost: 0},
	{"label":"User Task A (Sim39)", task: "User Task A", "id":"3c32b347-2b71-4308-9234-fe0e6149cbfd", "starting_time": 785, "ending_time": 790, delay: 0, cost: 0},
	{"label":"User Task B (Sim39)", task: "User Task B", "id":"531b3658-da18-417e-9119-ab2fe1d8751f", "starting_time": 790, "ending_time": 795, delay: 5, cost: 0},
	{"label":"Script Task (Sim39)", task: "Script Task", "id":"d7563ba9-c9f7-43f6-a839-778a238fe1d0", "starting_time": 795, "ending_time": 800, delay: 0, cost: 0},
	{"label":"Service Task (Sim40)", task: "Service Task", "id":"bab99573-00a8-41e6-adde-f57eed1eac53", "starting_time": 800, "ending_time": 805, delay: 0, cost: 0},
	{"label":"User Task B (Sim40)", task: "User Task B", "id":"c6951db9-b381-48f4-bedf-5a72329193be", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"User Task A (Sim40)", task: "User Task A", "id":"09d09b0e-e167-4aec-88b1-336a01821791", "starting_time": 810, "ending_time": 815, delay: 5, cost: 0},
	{"label":"Script Task (Sim40)", task: "Script Task", "id":"61f9f5cc-f6ac-4eeb-9ddd-68f19b81f343", "starting_time": 815, "ending_time": 820, delay: 0, cost: 0},
	{"label":"Service Task (Sim41)", task: "Service Task", "id":"c97afd73-78ab-462b-adc2-468933ff2959", "starting_time": 820, "ending_time": 825, delay: 0, cost: 0},
	{"label":"User Task A (Sim41)", task: "User Task A", "id":"83d70f61-6d74-47bf-8293-b475fdd40be4", "starting_time": 825, "ending_time": 830, delay: 0, cost: 0},
	{"label":"User Task B (Sim41)", task: "User Task B", "id":"0cb88001-8e5f-4ce7-9e26-688b8214ea14", "starting_time": 830, "ending_time": 835, delay: 5, cost: 0},
	{"label":"Script Task (Sim41)", task: "Script Task", "id":"38f26e99-0a65-4a86-ae32-bf0d9fed461e", "starting_time": 835, "ending_time": 840, delay: 0, cost: 0},
	{"label":"Service Task (Sim42)", task: "Service Task", "id":"357496bf-15fa-4557-9409-c4a796f75dbe", "starting_time": 840, "ending_time": 845, delay: 0, cost: 0},
	{"label":"User Task B (Sim42)", task: "User Task B", "id":"97820507-88d9-47fb-adbc-205425a83b3b", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"User Task A (Sim42)", task: "User Task A", "id":"a12eacce-09e0-436e-a227-4fc1adb7741b", "starting_time": 850, "ending_time": 855, delay: 5, cost: 0},
	{"label":"Script Task (Sim42)", task: "Script Task", "id":"10f50555-610d-4068-8de4-c2491d12808b", "starting_time": 855, "ending_time": 860, delay: 0, cost: 0},
	{"label":"Service Task (Sim43)", task: "Service Task", "id":"ce4c5034-b262-433a-ac18-8be7efb494ae", "starting_time": 860, "ending_time": 865, delay: 0, cost: 0},
	{"label":"User Task A (Sim43)", task: "User Task A", "id":"cbb732d1-d6ed-401b-90fb-abbec4b0f143", "starting_time": 865, "ending_time": 870, delay: 0, cost: 0},
	{"label":"User Task B (Sim43)", task: "User Task B", "id":"e145f7be-a4c2-4e04-b872-df5d952ad6cc", "starting_time": 870, "ending_time": 875, delay: 5, cost: 0},
	{"label":"Script Task (Sim43)", task: "Script Task", "id":"9e22a073-1671-469b-8fc3-4b630eb01801", "starting_time": 875, "ending_time": 880, delay: 0, cost: 0},
	{"label":"Service Task (Sim44)", task: "Service Task", "id":"e149c2b4-83bb-452c-b3bb-af946e68b1e0", "starting_time": 880, "ending_time": 885, delay: 0, cost: 0},
	{"label":"User Task B (Sim44)", task: "User Task B", "id":"954c05b8-a23e-4c73-8056-f29f92dd465b", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"User Task A (Sim44)", task: "User Task A", "id":"711a32b2-ae31-4fba-8cb9-39694faebdc4", "starting_time": 890, "ending_time": 895, delay: 5, cost: 0},
	{"label":"Script Task (Sim44)", task: "Script Task", "id":"b81a9b88-e97a-4171-b997-616f3468419c", "starting_time": 895, "ending_time": 900, delay: 0, cost: 0},
	{"label":"Service Task (Sim45)", task: "Service Task", "id":"59f59210-57e0-4455-b14a-74da9ffeba97", "starting_time": 900, "ending_time": 905, delay: 0, cost: 0},
	{"label":"User Task A (Sim45)", task: "User Task A", "id":"13ac341f-0bf0-48fc-b1cd-cd18a47c30c1", "starting_time": 905, "ending_time": 910, delay: 0, cost: 0},
	{"label":"User Task B (Sim45)", task: "User Task B", "id":"2a30b0ca-0c2c-49e7-a903-37c52ade389f", "starting_time": 910, "ending_time": 915, delay: 5, cost: 0},
	{"label":"Script Task (Sim45)", task: "Script Task", "id":"dd0fab8c-876b-48f5-a608-4a1f555893d1", "starting_time": 915, "ending_time": 920, delay: 0, cost: 0},
	{"label":"Service Task (Sim46)", task: "Service Task", "id":"0301526f-7c37-45ab-80ed-bbfae5a3a484", "starting_time": 920, "ending_time": 925, delay: 0, cost: 0},
	{"label":"User Task B (Sim46)", task: "User Task B", "id":"3aef0db9-d83f-49fc-a5a4-09b149170471", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"User Task A (Sim46)", task: "User Task A", "id":"53594a9f-e061-41ce-a1d7-134140282ad6", "starting_time": 930, "ending_time": 935, delay: 5, cost: 0},
	{"label":"Script Task (Sim46)", task: "Script Task", "id":"49d6aeec-dc20-48cf-bfda-d385381a63c3", "starting_time": 935, "ending_time": 940, delay: 0, cost: 0},
	{"label":"Service Task (Sim47)", task: "Service Task", "id":"11717de1-8eab-455b-8be1-0b5cadbaa06d", "starting_time": 940, "ending_time": 945, delay: 0, cost: 0},
	{"label":"User Task B (Sim47)", task: "User Task B", "id":"edf6a7ba-f0c0-4350-a178-7c3d12dae8f0", "starting_time": 945, "ending_time": 950, delay: 0, cost: 0},
	{"label":"User Task A (Sim47)", task: "User Task A", "id":"74032211-681a-4b66-bd25-ea6c0fe46a4c", "starting_time": 950, "ending_time": 955, delay: 5, cost: 0},
	{"label":"Script Task (Sim47)", task: "Script Task", "id":"2ba8ea89-31ef-4e24-9bf7-411b276f96bb", "starting_time": 955, "ending_time": 960, delay: 0, cost: 0},
	{"label":"Service Task (Sim48)", task: "Service Task", "id":"f8f4dd83-9980-48f8-8e83-6a37b539e603", "starting_time": 960, "ending_time": 965, delay: 0, cost: 0},
	{"label":"User Task A (Sim48)", task: "User Task A", "id":"fd13c04d-1e0d-4440-b3dd-0107c2c28391", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"User Task B (Sim48)", task: "User Task B", "id":"2c25e972-4642-4e41-ae73-49591fb8f1b9", "starting_time": 970, "ending_time": 975, delay: 5, cost: 0},
	{"label":"Script Task (Sim48)", task: "Script Task", "id":"faadc45c-d34c-4f8b-9041-898893ac29b7", "starting_time": 975, "ending_time": 980, delay: 0, cost: 0},
	{"label":"Service Task (Sim49)", task: "Service Task", "id":"aca8b9d1-8fac-446a-b280-47b51c175490", "starting_time": 980, "ending_time": 985, delay: 0, cost: 0},
	{"label":"User Task B (Sim49)", task: "User Task B", "id":"8c64a4c5-cc37-40f7-a9db-d72fc9a829bf", "starting_time": 985, "ending_time": 990, delay: 0, cost: 0},
	{"label":"User Task A (Sim49)", task: "User Task A", "id":"347056cc-4414-4e64-9e6d-c0f5fe85d82f", "starting_time": 990, "ending_time": 995, delay: 5, cost: 0},
	{"label":"Script Task (Sim49)", task: "Script Task", "id":"70e12bb4-f9d4-41da-bfbc-67ced2c61e99", "starting_time": 995, "ending_time": 1000, delay: 0, cost: 0},
	{"label":"Service Task (Sim50)", task: "Service Task", "id":"ecfb7f53-f7df-4ccf-920c-6b54bf07f99a", "starting_time": 1000, "ending_time": 1005, delay: 0, cost: 0},
	{"label":"User Task B (Sim50)", task: "User Task B", "id":"e6920d66-6a01-42dc-a1a7-a52816105b97", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"User Task A (Sim50)", task: "User Task A", "id":"e7a52ceb-e6cc-4a59-9b6f-27713b738a66", "starting_time": 1010, "ending_time": 1015, delay: 5, cost: 0},
	{"label":"Script Task (Sim50)", task: "Script Task", "id":"09bd0f09-6bf5-43e3-9a67-f6f90311ddff", "starting_time": 1015, "ending_time": 1020, delay: 0, cost: 0},
	{"label":"Service Task (Sim51)", task: "Service Task", "id":"6888c24c-6086-4b9e-a657-c9fc8ce3a484", "starting_time": 1020, "ending_time": 1025, delay: 0, cost: 0},
	{"label":"User Task A (Sim51)", task: "User Task A", "id":"b9fe6fac-69db-4885-b45f-abe90f24b4b8", "starting_time": 1025, "ending_time": 1030, delay: 0, cost: 0},
	{"label":"User Task B (Sim51)", task: "User Task B", "id":"2fa00185-6e84-4fff-a334-fba2d668d1b1", "starting_time": 1030, "ending_time": 1035, delay: 5, cost: 0},
	{"label":"Script Task (Sim51)", task: "Script Task", "id":"c3c8b332-6e87-4c17-b17a-dcd2f628093e", "starting_time": 1035, "ending_time": 1040, delay: 0, cost: 0},
	{"label":"Service Task (Sim52)", task: "Service Task", "id":"edb9e7c9-9d62-4d81-b215-4a4622672b99", "starting_time": 1040, "ending_time": 1045, delay: 0, cost: 0},
	{"label":"User Task A (Sim52)", task: "User Task A", "id":"e7cdd299-ecc1-4996-bc1d-eb37354379b5", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"User Task B (Sim52)", task: "User Task B", "id":"63372ff2-eed8-406d-b7d1-d608d6aa39a2", "starting_time": 1050, "ending_time": 1055, delay: 5, cost: 0},
	{"label":"Script Task (Sim52)", task: "Script Task", "id":"a136cea1-da1b-4988-8f18-6414b9123e53", "starting_time": 1055, "ending_time": 1060, delay: 0, cost: 0},
	{"label":"Service Task (Sim53)", task: "Service Task", "id":"89ba5997-737f-4c62-b94a-debe530184e6", "starting_time": 1060, "ending_time": 1065, delay: 0, cost: 0},
	{"label":"User Task B (Sim53)", task: "User Task B", "id":"1bac2037-e2fc-408f-9e97-997a7fb821da", "starting_time": 1065, "ending_time": 1070, delay: 0, cost: 0},
	{"label":"User Task A (Sim53)", task: "User Task A", "id":"3a0bffe3-d89e-4186-a060-fac1240863ac", "starting_time": 1070, "ending_time": 1075, delay: 5, cost: 0},
	{"label":"Script Task (Sim53)", task: "Script Task", "id":"32290e6a-451e-4382-b745-c5df9030fc80", "starting_time": 1075, "ending_time": 1080, delay: 0, cost: 0},
	{"label":"Service Task (Sim54)", task: "Service Task", "id":"64522237-cecd-4ce4-bc74-2b95d3b0151c", "starting_time": 1080, "ending_time": 1085, delay: 0, cost: 0},
	{"label":"User Task B (Sim54)", task: "User Task B", "id":"84d563bc-047b-41a9-a6e2-4edd0a157c30", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"User Task A (Sim54)", task: "User Task A", "id":"9d32e1f0-6d99-43ac-964b-ac5599a7c58c", "starting_time": 1090, "ending_time": 1095, delay: 5, cost: 0},
	{"label":"Script Task (Sim54)", task: "Script Task", "id":"32a91458-d432-4843-8504-bbb92e4c1a4c", "starting_time": 1095, "ending_time": 1100, delay: 0, cost: 0},
	{"label":"Service Task (Sim55)", task: "Service Task", "id":"b4f0d480-d862-405a-8f5b-b88a2898f3be", "starting_time": 1100, "ending_time": 1105, delay: 0, cost: 0},
	{"label":"User Task B (Sim55)", task: "User Task B", "id":"905f0eaf-93bd-4223-b27f-a1ac18d4862e", "starting_time": 1105, "ending_time": 1110, delay: 0, cost: 0},
	{"label":"User Task A (Sim55)", task: "User Task A", "id":"dfd0337f-2162-46bb-b47d-8e015c02eb4f", "starting_time": 1110, "ending_time": 1115, delay: 5, cost: 0},
	{"label":"Script Task (Sim55)", task: "Script Task", "id":"0a1c625d-fa69-4e35-8faf-ab99523f5b61", "starting_time": 1115, "ending_time": 1120, delay: 0, cost: 0},
	{"label":"Service Task (Sim56)", task: "Service Task", "id":"c09a2a72-7b46-49f2-b9d4-03fced6fceee", "starting_time": 1120, "ending_time": 1125, delay: 0, cost: 0},
	{"label":"User Task B (Sim56)", task: "User Task B", "id":"b5682387-dc9a-4a9a-b68f-b0a18a1e0020", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"User Task A (Sim56)", task: "User Task A", "id":"f81a3ab6-7c5a-4b6a-be20-7650af6d3167", "starting_time": 1130, "ending_time": 1135, delay: 5, cost: 0},
	{"label":"Script Task (Sim56)", task: "Script Task", "id":"d8cc6ba3-69b6-44a8-9f43-d323110f8a73", "starting_time": 1135, "ending_time": 1140, delay: 0, cost: 0},
	{"label":"Service Task (Sim57)", task: "Service Task", "id":"d4bbe84d-424b-4e6a-88f3-1686411d218d", "starting_time": 1140, "ending_time": 1145, delay: 0, cost: 0},
	{"label":"User Task B (Sim57)", task: "User Task B", "id":"93199453-37f8-4818-86f5-eb8ad49ca5f7", "starting_time": 1145, "ending_time": 1150, delay: 0, cost: 0},
	{"label":"User Task A (Sim57)", task: "User Task A", "id":"db0b14f8-e143-408a-9865-4a6dea373ba7", "starting_time": 1150, "ending_time": 1155, delay: 5, cost: 0},
	{"label":"Script Task (Sim57)", task: "Script Task", "id":"1564adf5-86dd-47f7-b2b3-a6e56b0c1bf9", "starting_time": 1155, "ending_time": 1160, delay: 0, cost: 0},
	{"label":"Service Task (Sim58)", task: "Service Task", "id":"62f21f46-5eaf-4434-8777-8e878ec32cd1", "starting_time": 1160, "ending_time": 1165, delay: 0, cost: 0},
	{"label":"User Task A (Sim58)", task: "User Task A", "id":"ea57bbbd-89f7-49fb-a062-59f59f8b2318", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"User Task B (Sim58)", task: "User Task B", "id":"0c44f301-0c4b-494e-a998-3d5862087fba", "starting_time": 1170, "ending_time": 1175, delay: 5, cost: 0},
	{"label":"Script Task (Sim58)", task: "Script Task", "id":"92e6e881-238c-4494-b23b-a26f78d81192", "starting_time": 1175, "ending_time": 1180, delay: 0, cost: 0},
	{"label":"Service Task (Sim59)", task: "Service Task", "id":"936a80c4-a9db-4e1f-b349-e7feef2de2db", "starting_time": 1180, "ending_time": 1185, delay: 0, cost: 0},
	{"label":"User Task A (Sim59)", task: "User Task A", "id":"d1bff30a-b8bf-4ce6-954b-981c9e226e25", "starting_time": 1185, "ending_time": 1190, delay: 0, cost: 0},
	{"label":"User Task B (Sim59)", task: "User Task B", "id":"62ecb169-f542-4b4a-940e-f201c06b8e50", "starting_time": 1190, "ending_time": 1195, delay: 5, cost: 0},
	{"label":"Script Task (Sim59)", task: "Script Task", "id":"1d442d93-1770-4f42-8d58-d1ff26a3e786", "starting_time": 1195, "ending_time": 1200, delay: 0, cost: 0},
	{"label":"Service Task (Sim60)", task: "Service Task", "id":"eca61c17-c64d-484f-a004-a1d32443ba3f", "starting_time": 1200, "ending_time": 1205, delay: 0, cost: 0},
	{"label":"User Task B (Sim60)", task: "User Task B", "id":"c65ec326-776f-4555-9916-02e3a43f918d", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"User Task A (Sim60)", task: "User Task A", "id":"5a56ff7a-ab6b-483b-8c43-d0f351c44e7c", "starting_time": 1210, "ending_time": 1215, delay: 5, cost: 0},
	{"label":"Script Task (Sim60)", task: "Script Task", "id":"fe059873-25de-45e7-bc54-3a3836104251", "starting_time": 1215, "ending_time": 1220, delay: 0, cost: 0},
	{"label":"Service Task (Sim61)", task: "Service Task", "id":"693816df-8ca6-4c6e-b5ee-f795709f3bcd", "starting_time": 1220, "ending_time": 1225, delay: 0, cost: 0},
	{"label":"User Task B (Sim61)", task: "User Task B", "id":"a60efa50-8e16-4cfb-9437-a8d9edc0c753", "starting_time": 1225, "ending_time": 1230, delay: 0, cost: 0},
	{"label":"User Task A (Sim61)", task: "User Task A", "id":"49612e56-8b9b-48a5-a478-0b078476277a", "starting_time": 1230, "ending_time": 1235, delay: 5, cost: 0},
	{"label":"Script Task (Sim61)", task: "Script Task", "id":"7ae102c3-38b5-45f0-afcd-610ca8343bcd", "starting_time": 1235, "ending_time": 1240, delay: 0, cost: 0},
	{"label":"Service Task (Sim62)", task: "Service Task", "id":"aaff73b4-4c33-44b5-b97f-17d7fc95fd36", "starting_time": 1240, "ending_time": 1245, delay: 0, cost: 0},
	{"label":"User Task B (Sim62)", task: "User Task B", "id":"d5e268c5-61ad-43bf-8f33-ee323f6ce64c", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"User Task A (Sim62)", task: "User Task A", "id":"2de20805-44fa-4b63-be5c-61a61d88afd0", "starting_time": 1250, "ending_time": 1255, delay: 5, cost: 0},
	{"label":"Script Task (Sim62)", task: "Script Task", "id":"2676b4a8-ad50-4ae6-a267-6db1c70cb0c1", "starting_time": 1255, "ending_time": 1260, delay: 0, cost: 0},
	{"label":"Service Task (Sim63)", task: "Service Task", "id":"2a55d6dc-c7fd-446c-a316-eef303d2dcfd", "starting_time": 1260, "ending_time": 1265, delay: 0, cost: 0},
	{"label":"User Task A (Sim63)", task: "User Task A", "id":"d327f362-020d-43ee-a07d-4d730839a920", "starting_time": 1265, "ending_time": 1270, delay: 0, cost: 0},
	{"label":"User Task B (Sim63)", task: "User Task B", "id":"675e64bd-7952-45cd-8349-f4fe45f6e016", "starting_time": 1270, "ending_time": 1275, delay: 5, cost: 0},
	{"label":"Script Task (Sim63)", task: "Script Task", "id":"6c40d7cf-df32-440c-b5d5-ccc106d1c902", "starting_time": 1275, "ending_time": 1280, delay: 0, cost: 0},
	{"label":"Service Task (Sim64)", task: "Service Task", "id":"18a44e7b-28dd-4f37-98f1-5308dbaccf7c", "starting_time": 1280, "ending_time": 1285, delay: 0, cost: 0},
	{"label":"User Task B (Sim64)", task: "User Task B", "id":"c5bea4a6-2059-4034-b4d6-1c1367cff5fd", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"User Task A (Sim64)", task: "User Task A", "id":"3858ee54-e0ec-4c12-89cd-5fa9ebdd2f1b", "starting_time": 1290, "ending_time": 1295, delay: 5, cost: 0},
	{"label":"Script Task (Sim64)", task: "Script Task", "id":"4d564714-90d9-4928-add2-bfde28d9ed4b", "starting_time": 1295, "ending_time": 1300, delay: 0, cost: 0},
	{"label":"Service Task (Sim65)", task: "Service Task", "id":"bf0baccf-797a-4400-8dea-08c2b0c3c8cf", "starting_time": 1300, "ending_time": 1305, delay: 0, cost: 0},
	{"label":"User Task B (Sim65)", task: "User Task B", "id":"b332f3b9-80ed-4508-bab4-188ad7bb45f1", "starting_time": 1305, "ending_time": 1310, delay: 0, cost: 0},
	{"label":"User Task A (Sim65)", task: "User Task A", "id":"13ea9ffe-ee8a-4891-9955-4893dc8ee8ca", "starting_time": 1310, "ending_time": 1315, delay: 5, cost: 0},
	{"label":"Script Task (Sim65)", task: "Script Task", "id":"83b0117e-5b8c-4002-8af3-53c4b95294d4", "starting_time": 1315, "ending_time": 1320, delay: 0, cost: 0},
	{"label":"Service Task (Sim66)", task: "Service Task", "id":"9107cf71-12b7-4062-be34-4b6a009b6455", "starting_time": 1320, "ending_time": 1325, delay: 0, cost: 0},
	{"label":"User Task B (Sim66)", task: "User Task B", "id":"3016751d-c3c0-4d9d-9a38-eeb6eefd97bf", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"User Task A (Sim66)", task: "User Task A", "id":"59a727f1-10df-4c27-bd71-5f1d20ef95a2", "starting_time": 1330, "ending_time": 1335, delay: 5, cost: 0},
	{"label":"Script Task (Sim66)", task: "Script Task", "id":"9ff74055-0314-4a9e-8ca3-0ea987b1e2b6", "starting_time": 1335, "ending_time": 1340, delay: 0, cost: 0},
	{"label":"Service Task (Sim67)", task: "Service Task", "id":"f3f2b71d-f904-49b5-8a00-e6cbf43803cd", "starting_time": 1340, "ending_time": 1345, delay: 0, cost: 0},
	{"label":"User Task A (Sim67)", task: "User Task A", "id":"fb7c424e-7010-41e7-9180-9aa32074a993", "starting_time": 1345, "ending_time": 1350, delay: 0, cost: 0},
	{"label":"User Task B (Sim67)", task: "User Task B", "id":"38fb24c7-c334-4708-838c-9ce9137c38f4", "starting_time": 1350, "ending_time": 1355, delay: 5, cost: 0},
	{"label":"Script Task (Sim67)", task: "Script Task", "id":"dfe04f6a-6795-4e7e-bd54-dd867962b797", "starting_time": 1355, "ending_time": 1360, delay: 0, cost: 0},
	{"label":"Service Task (Sim68)", task: "Service Task", "id":"8486c7d0-5e31-4f3a-ba23-090b1882321e", "starting_time": 1360, "ending_time": 1365, delay: 0, cost: 0},
	{"label":"User Task B (Sim68)", task: "User Task B", "id":"fffbfecc-9ae9-4468-bca4-8b587438d229", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"User Task A (Sim68)", task: "User Task A", "id":"0818953f-37e2-4e3d-ad0b-baea70b3e635", "starting_time": 1370, "ending_time": 1375, delay: 5, cost: 0},
	{"label":"Script Task (Sim68)", task: "Script Task", "id":"6df45ca3-59a7-4280-bb75-f5d947143edc", "starting_time": 1375, "ending_time": 1380, delay: 0, cost: 0},
	{"label":"Service Task (Sim69)", task: "Service Task", "id":"07e9d34c-c2be-4f58-8e55-7feef671f5c1", "starting_time": 1380, "ending_time": 1385, delay: 0, cost: 0},
	{"label":"User Task B (Sim69)", task: "User Task B", "id":"97ccd812-92d8-4adf-a844-b1d91d1ee386", "starting_time": 1385, "ending_time": 1390, delay: 0, cost: 0},
	{"label":"User Task A (Sim69)", task: "User Task A", "id":"42db47cf-5dc8-4384-9f59-67dfbcbb9c26", "starting_time": 1390, "ending_time": 1395, delay: 5, cost: 0},
	{"label":"Script Task (Sim69)", task: "Script Task", "id":"58a23af5-16f7-4e6c-9be0-cf2b5ae73923", "starting_time": 1395, "ending_time": 1400, delay: 0, cost: 0},
	{"label":"Service Task (Sim70)", task: "Service Task", "id":"12144f10-106b-4d4c-8fd1-5060e5a6e0da", "starting_time": 1400, "ending_time": 1405, delay: 0, cost: 0},
	{"label":"User Task B (Sim70)", task: "User Task B", "id":"a5a94ab0-e1f3-4a9f-8ee4-0f5f1ab414e4", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"User Task A (Sim70)", task: "User Task A", "id":"2a0ad420-2b1d-4f38-a348-1fabf3ba9beb", "starting_time": 1410, "ending_time": 1415, delay: 5, cost: 0},
	{"label":"Script Task (Sim70)", task: "Script Task", "id":"053a4adc-9c3c-4954-9967-fe3633a890bc", "starting_time": 1415, "ending_time": 1420, delay: 0, cost: 0},
	{"label":"Service Task (Sim71)", task: "Service Task", "id":"3d8e0cab-4a03-4c1e-b8b8-62d37b72db36", "starting_time": 1420, "ending_time": 1425, delay: 0, cost: 0},
	{"label":"User Task B (Sim71)", task: "User Task B", "id":"964f4f3f-7fc5-4cb0-a432-031adfc0e3a6", "starting_time": 1425, "ending_time": 1430, delay: 0, cost: 0},
	{"label":"User Task A (Sim71)", task: "User Task A", "id":"b57891f2-7f28-4470-9cfc-a2c505d3aaef", "starting_time": 1430, "ending_time": 1435, delay: 5, cost: 0},
	{"label":"Script Task (Sim71)", task: "Script Task", "id":"444b2bf3-1c27-499d-8bb1-f7106d315431", "starting_time": 1435, "ending_time": 1440, delay: 0, cost: 0},
	{"label":"Service Task (Sim72)", task: "Service Task", "id":"99fd7654-40c8-42a7-bf03-673a0274cce2", "starting_time": 1440, "ending_time": 1445, delay: 0, cost: 0},
	{"label":"User Task B (Sim72)", task: "User Task B", "id":"1af506ec-03ab-436c-90eb-85317862e5ae", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"User Task A (Sim72)", task: "User Task A", "id":"6283a56e-6c06-4106-ae13-ad29aceef261", "starting_time": 1450, "ending_time": 1455, delay: 5, cost: 0},
	{"label":"Script Task (Sim72)", task: "Script Task", "id":"fe25a427-79e9-4b27-b3e0-f86daf73e3d1", "starting_time": 1455, "ending_time": 1460, delay: 0, cost: 0},
	{"label":"Service Task (Sim73)", task: "Service Task", "id":"ab94ab5f-3670-4ced-89bf-312190bc8852", "starting_time": 1460, "ending_time": 1465, delay: 0, cost: 0},
	{"label":"User Task A (Sim73)", task: "User Task A", "id":"c04fa061-c52d-403e-8582-ea2dde6ab906", "starting_time": 1465, "ending_time": 1470, delay: 0, cost: 0},
	{"label":"User Task B (Sim73)", task: "User Task B", "id":"f83d3f0f-3cdc-4004-a749-6811d6a90b0f", "starting_time": 1470, "ending_time": 1475, delay: 5, cost: 0},
	{"label":"Script Task (Sim73)", task: "Script Task", "id":"e845708a-6e53-41e5-b9d6-10957c6269b2", "starting_time": 1475, "ending_time": 1480, delay: 0, cost: 0},
	{"label":"Service Task (Sim74)", task: "Service Task", "id":"5d5561fe-7fde-4516-a311-3b1db17501b4", "starting_time": 1480, "ending_time": 1485, delay: 0, cost: 0},
	{"label":"User Task A (Sim74)", task: "User Task A", "id":"cebaa8b5-3f0d-4900-a994-17f5d276fcc2", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"User Task B (Sim74)", task: "User Task B", "id":"33f01686-22be-4bcd-931a-3612c8336873", "starting_time": 1490, "ending_time": 1495, delay: 5, cost: 0},
	{"label":"Script Task (Sim74)", task: "Script Task", "id":"413f3e06-17aa-48a2-a051-e312ef5ea8ff", "starting_time": 1495, "ending_time": 1500, delay: 0, cost: 0},
	{"label":"Service Task (Sim75)", task: "Service Task", "id":"48f3b322-5161-4087-a4ff-281a7f0cbb6c", "starting_time": 1500, "ending_time": 1505, delay: 0, cost: 0},
	{"label":"User Task A (Sim75)", task: "User Task A", "id":"b7fccfc3-aafb-400f-bf0a-5c3c3481aa71", "starting_time": 1505, "ending_time": 1510, delay: 0, cost: 0},
	{"label":"User Task B (Sim75)", task: "User Task B", "id":"f62d9768-037e-4fc1-a68d-bce94e9d3de6", "starting_time": 1510, "ending_time": 1515, delay: 5, cost: 0},
	{"label":"Script Task (Sim75)", task: "Script Task", "id":"673ebd85-3676-44c4-a9a8-e3baf8808afc", "starting_time": 1515, "ending_time": 1520, delay: 0, cost: 0},
	{"label":"Service Task (Sim76)", task: "Service Task", "id":"6d13a560-7df3-4e03-ac48-f0d9bdd6d6a7", "starting_time": 1520, "ending_time": 1525, delay: 0, cost: 0},
	{"label":"User Task B (Sim76)", task: "User Task B", "id":"f46c9f3a-8284-4239-87a1-ad24eab8d66f", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"User Task A (Sim76)", task: "User Task A", "id":"0f3bcb11-1534-4858-b405-13ff21105d9f", "starting_time": 1530, "ending_time": 1535, delay: 5, cost: 0},
	{"label":"Script Task (Sim76)", task: "Script Task", "id":"ceeecf19-48b0-44c0-927c-6736d818dcd8", "starting_time": 1535, "ending_time": 1540, delay: 0, cost: 0},
	{"label":"Service Task (Sim77)", task: "Service Task", "id":"364f51a8-4106-4199-8fad-f9bb57633ba3", "starting_time": 1540, "ending_time": 1545, delay: 0, cost: 0},
	{"label":"User Task A (Sim77)", task: "User Task A", "id":"c1bd4d09-67ab-42e1-bd5d-148d8c0b743c", "starting_time": 1545, "ending_time": 1550, delay: 0, cost: 0},
	{"label":"User Task B (Sim77)", task: "User Task B", "id":"e2a682df-f5ed-4a13-932d-c15750e7b306", "starting_time": 1550, "ending_time": 1555, delay: 5, cost: 0},
	{"label":"Script Task (Sim77)", task: "Script Task", "id":"430043a5-d93a-4f26-9ef1-4a2c17723c80", "starting_time": 1555, "ending_time": 1560, delay: 0, cost: 0},
	{"label":"Service Task (Sim78)", task: "Service Task", "id":"1ff0b4a2-51d6-48fe-92d6-d32af3993c74", "starting_time": 1560, "ending_time": 1565, delay: 0, cost: 0},
	{"label":"User Task A (Sim78)", task: "User Task A", "id":"df11cedf-ee2c-4aa3-ae16-26898ef1b867", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"User Task B (Sim78)", task: "User Task B", "id":"73e1081c-cb6b-4d3c-98f8-017055a3433b", "starting_time": 1570, "ending_time": 1575, delay: 5, cost: 0},
	{"label":"Script Task (Sim78)", task: "Script Task", "id":"89aa9a32-d830-4ff8-a3b4-7ae6f98330c6", "starting_time": 1575, "ending_time": 1580, delay: 0, cost: 0},
	{"label":"Service Task (Sim79)", task: "Service Task", "id":"f0dcdedd-2a50-4f96-aec8-d953a10f2183", "starting_time": 1580, "ending_time": 1585, delay: 0, cost: 0},
	{"label":"User Task A (Sim79)", task: "User Task A", "id":"2d76e9a9-7d80-445d-b95f-89edfd3dcded", "starting_time": 1585, "ending_time": 1590, delay: 0, cost: 0},
	{"label":"User Task B (Sim79)", task: "User Task B", "id":"6d7fbb9d-cc26-49fa-8ed1-0c2fbb4e69af", "starting_time": 1590, "ending_time": 1595, delay: 5, cost: 0},
	{"label":"Script Task (Sim79)", task: "Script Task", "id":"358cf623-6e73-49a2-a1d3-442d81716ade", "starting_time": 1595, "ending_time": 1600, delay: 0, cost: 0},
	{"label":"Service Task (Sim80)", task: "Service Task", "id":"8817e7ee-9203-470e-a802-04f8104ea5df", "starting_time": 1600, "ending_time": 1605, delay: 0, cost: 0},
	{"label":"User Task A (Sim80)", task: "User Task A", "id":"82f8cfe9-fc38-4be5-af3d-20a2bbfc79f4", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"User Task B (Sim80)", task: "User Task B", "id":"182f6280-1db2-4a0c-bbd5-8be8b6289ab5", "starting_time": 1610, "ending_time": 1615, delay: 5, cost: 0},
	{"label":"Script Task (Sim80)", task: "Script Task", "id":"06f73372-e5ef-488e-b3da-946d7a5254e3", "starting_time": 1615, "ending_time": 1620, delay: 0, cost: 0},
	{"label":"Service Task (Sim81)", task: "Service Task", "id":"d10c1efd-6e1c-4123-b430-2452a4a831e0", "starting_time": 1620, "ending_time": 1625, delay: 0, cost: 0},
	{"label":"User Task A (Sim81)", task: "User Task A", "id":"8a408212-98c6-496f-bb0a-575959bac051", "starting_time": 1625, "ending_time": 1630, delay: 0, cost: 0},
	{"label":"User Task B (Sim81)", task: "User Task B", "id":"0de47be9-c39c-4ee7-8456-755ce83de731", "starting_time": 1630, "ending_time": 1635, delay: 5, cost: 0},
	{"label":"Script Task (Sim81)", task: "Script Task", "id":"a6abe662-3954-4fd9-91f4-d094d518fa03", "starting_time": 1635, "ending_time": 1640, delay: 0, cost: 0},
	{"label":"Service Task (Sim82)", task: "Service Task", "id":"26a65a4a-d95b-471c-a3bc-d931a62aace5", "starting_time": 1640, "ending_time": 1645, delay: 0, cost: 0},
	{"label":"User Task A (Sim82)", task: "User Task A", "id":"9b9eaa43-7675-4d38-8542-603312ae5f3c", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"User Task B (Sim82)", task: "User Task B", "id":"50014dfd-a7ae-498f-a4ba-f1de70bc5858", "starting_time": 1650, "ending_time": 1655, delay: 5, cost: 0},
	{"label":"Script Task (Sim82)", task: "Script Task", "id":"3ac3da51-3e21-47d4-9739-6ffe075e927f", "starting_time": 1655, "ending_time": 1660, delay: 0, cost: 0},
	{"label":"Service Task (Sim83)", task: "Service Task", "id":"e50912d3-4c81-4cac-862e-4305efa53021", "starting_time": 1660, "ending_time": 1665, delay: 0, cost: 0},
	{"label":"User Task B (Sim83)", task: "User Task B", "id":"8e1978e7-678e-4910-9d78-8a91a2ec0b8a", "starting_time": 1665, "ending_time": 1670, delay: 0, cost: 0},
	{"label":"User Task A (Sim83)", task: "User Task A", "id":"99cb61ab-0bc0-4296-912e-20791d70a6f9", "starting_time": 1670, "ending_time": 1675, delay: 5, cost: 0},
	{"label":"Script Task (Sim83)", task: "Script Task", "id":"b5ee6643-d363-48bb-bfed-b2bf8d9cd02a", "starting_time": 1675, "ending_time": 1680, delay: 0, cost: 0},
	{"label":"Service Task (Sim84)", task: "Service Task", "id":"e3cb0215-60ce-4585-bdd1-f508d88e4291", "starting_time": 1680, "ending_time": 1685, delay: 0, cost: 0},
	{"label":"User Task A (Sim84)", task: "User Task A", "id":"a8a611a9-75ca-4a0c-9286-e867d7b2129d", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"User Task B (Sim84)", task: "User Task B", "id":"c95d920c-6518-4342-90a1-c0211c5e197e", "starting_time": 1690, "ending_time": 1695, delay: 5, cost: 0},
	{"label":"Script Task (Sim84)", task: "Script Task", "id":"604e1cc9-fe5c-4029-b06c-6735c72ed651", "starting_time": 1695, "ending_time": 1700, delay: 0, cost: 0},
	{"label":"Service Task (Sim85)", task: "Service Task", "id":"d5905d12-abdb-4ecc-a3a8-4557dce9fedf", "starting_time": 1700, "ending_time": 1705, delay: 0, cost: 0},
	{"label":"User Task B (Sim85)", task: "User Task B", "id":"0da86c3d-f550-49ff-aa46-0d751889a391", "starting_time": 1705, "ending_time": 1710, delay: 0, cost: 0},
	{"label":"User Task A (Sim85)", task: "User Task A", "id":"691940d2-f599-44c4-a000-eba53c8bd6d4", "starting_time": 1710, "ending_time": 1715, delay: 5, cost: 0},
	{"label":"Script Task (Sim85)", task: "Script Task", "id":"1d5f56c9-6888-44d1-ac01-b38b506217fe", "starting_time": 1715, "ending_time": 1720, delay: 0, cost: 0},
	{"label":"Service Task (Sim86)", task: "Service Task", "id":"eb6a3782-b09c-4dc2-93eb-658cf337d3e6", "starting_time": 1720, "ending_time": 1725, delay: 0, cost: 0},
	{"label":"User Task B (Sim86)", task: "User Task B", "id":"d3b0002c-e4b6-4edd-8880-f5d7c050d89a", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"User Task A (Sim86)", task: "User Task A", "id":"44a4ba33-9c3b-4121-8c04-d0a6a0659e28", "starting_time": 1730, "ending_time": 1735, delay: 5, cost: 0},
	{"label":"Script Task (Sim86)", task: "Script Task", "id":"e98a0307-e70c-4b41-af6d-0e050890ca3f", "starting_time": 1735, "ending_time": 1740, delay: 0, cost: 0},
	{"label":"Service Task (Sim87)", task: "Service Task", "id":"9cc715e6-a193-4dc9-8ad2-0060fc292a83", "starting_time": 1740, "ending_time": 1745, delay: 0, cost: 0},
	{"label":"User Task B (Sim87)", task: "User Task B", "id":"847bdc02-caee-47ae-8d12-54f23967ff1f", "starting_time": 1745, "ending_time": 1750, delay: 0, cost: 0},
	{"label":"User Task A (Sim87)", task: "User Task A", "id":"5862239d-e27c-4d8a-918f-bda321a0bd93", "starting_time": 1750, "ending_time": 1755, delay: 5, cost: 0},
	{"label":"Script Task (Sim87)", task: "Script Task", "id":"90a40cf7-d28d-456b-b08f-0a665d0f278a", "starting_time": 1755, "ending_time": 1760, delay: 0, cost: 0},
	{"label":"Service Task (Sim88)", task: "Service Task", "id":"41627c0c-3cf5-4597-ab89-65456145e523", "starting_time": 1760, "ending_time": 1765, delay: 0, cost: 0},
	{"label":"User Task B (Sim88)", task: "User Task B", "id":"b3bc7643-6ee8-41b9-bae4-6c21af40f946", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"User Task A (Sim88)", task: "User Task A", "id":"bd77e2ca-7e24-4582-80b2-85d180584c76", "starting_time": 1770, "ending_time": 1775, delay: 5, cost: 0},
	{"label":"Script Task (Sim88)", task: "Script Task", "id":"9d813cad-50da-4a1b-8704-deb65b214656", "starting_time": 1775, "ending_time": 1780, delay: 0, cost: 0},
	{"label":"Service Task (Sim89)", task: "Service Task", "id":"ba0eb517-9606-44c4-a253-93e30d347d2d", "starting_time": 1780, "ending_time": 1785, delay: 0, cost: 0},
	{"label":"User Task B (Sim89)", task: "User Task B", "id":"c8f8b73d-3820-47f2-961d-6af01982f0e8", "starting_time": 1785, "ending_time": 1790, delay: 0, cost: 0},
	{"label":"User Task A (Sim89)", task: "User Task A", "id":"0a734d3f-7312-4349-8efc-51139d2d5a4f", "starting_time": 1790, "ending_time": 1795, delay: 5, cost: 0},
	{"label":"Script Task (Sim89)", task: "Script Task", "id":"ad746585-92d5-409c-91c7-3b7d0f85b8ab", "starting_time": 1795, "ending_time": 1800, delay: 0, cost: 0},
	{"label":"Service Task (Sim90)", task: "Service Task", "id":"037e37bf-55f5-4b33-99ae-8b6b704bbfd7", "starting_time": 1800, "ending_time": 1805, delay: 0, cost: 0},
	{"label":"User Task A (Sim90)", task: "User Task A", "id":"ab5a5ce0-35ad-46b5-8d4b-a11f0cf10ec9", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"User Task B (Sim90)", task: "User Task B", "id":"2e54eba3-06ed-4388-a616-9f488aabcdd0", "starting_time": 1810, "ending_time": 1815, delay: 5, cost: 0},
	{"label":"Script Task (Sim90)", task: "Script Task", "id":"2f90a78e-442f-4d19-b9a1-5b873fb13e17", "starting_time": 1815, "ending_time": 1820, delay: 0, cost: 0},
	{"label":"Service Task (Sim91)", task: "Service Task", "id":"4c26307d-b1a6-44ee-aef3-0a07378d5c3a", "starting_time": 1820, "ending_time": 1825, delay: 0, cost: 0},
	{"label":"User Task B (Sim91)", task: "User Task B", "id":"87283e43-c2bc-4691-9d10-7426f0c7028b", "starting_time": 1825, "ending_time": 1830, delay: 0, cost: 0},
	{"label":"User Task A (Sim91)", task: "User Task A", "id":"0ec4fbd8-960e-424d-b79d-09cb3acc9eee", "starting_time": 1830, "ending_time": 1835, delay: 5, cost: 0},
	{"label":"Script Task (Sim91)", task: "Script Task", "id":"47a25248-66f9-49d4-8b37-28a1282a036a", "starting_time": 1835, "ending_time": 1840, delay: 0, cost: 0},
	{"label":"Service Task (Sim92)", task: "Service Task", "id":"6833c155-96c4-4262-b8df-1835eed82753", "starting_time": 1840, "ending_time": 1845, delay: 0, cost: 0},
	{"label":"User Task B (Sim92)", task: "User Task B", "id":"83d9704e-2a6e-458a-a3b4-e96875fe067e", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"User Task A (Sim92)", task: "User Task A", "id":"48483e94-f634-4692-8013-9c0afb8b3b6e", "starting_time": 1850, "ending_time": 1855, delay: 5, cost: 0},
	{"label":"Script Task (Sim92)", task: "Script Task", "id":"c4ca3a11-91b4-4186-9790-df0fc04b8fa5", "starting_time": 1855, "ending_time": 1860, delay: 0, cost: 0},
	{"label":"Service Task (Sim93)", task: "Service Task", "id":"fcd9d777-c2d5-4f3c-9e04-d41178a7134d", "starting_time": 1860, "ending_time": 1865, delay: 0, cost: 0},
	{"label":"User Task A (Sim93)", task: "User Task A", "id":"f6c33323-75e9-4df1-9c31-f932ba84c62f", "starting_time": 1865, "ending_time": 1870, delay: 0, cost: 0},
	{"label":"User Task B (Sim93)", task: "User Task B", "id":"068ab3e3-dcab-4184-ad8c-d54e004652d2", "starting_time": 1870, "ending_time": 1875, delay: 5, cost: 0},
	{"label":"Script Task (Sim93)", task: "Script Task", "id":"5d06525f-9d89-4225-b13a-55fa42645441", "starting_time": 1875, "ending_time": 1880, delay: 0, cost: 0},
	{"label":"Service Task (Sim94)", task: "Service Task", "id":"14a4e746-e34b-4f15-bb0a-5baa9a842419", "starting_time": 1880, "ending_time": 1885, delay: 0, cost: 0},
	{"label":"User Task B (Sim94)", task: "User Task B", "id":"60099fd0-5c14-439e-aed8-1505612ac850", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"User Task A (Sim94)", task: "User Task A", "id":"79353634-7bd6-4cae-8fa0-9ade6b415f62", "starting_time": 1890, "ending_time": 1895, delay: 5, cost: 0},
	{"label":"Script Task (Sim94)", task: "Script Task", "id":"87e57115-ca9c-441a-a8d7-6c49f92147b3", "starting_time": 1895, "ending_time": 1900, delay: 0, cost: 0},
	{"label":"Service Task (Sim95)", task: "Service Task", "id":"1624e535-1528-4270-8326-2a464cd730da", "starting_time": 1900, "ending_time": 1905, delay: 0, cost: 0},
	{"label":"User Task A (Sim95)", task: "User Task A", "id":"d1e53231-734b-4318-a60f-a7ae9026c78a", "starting_time": 1905, "ending_time": 1910, delay: 0, cost: 0},
	{"label":"User Task B (Sim95)", task: "User Task B", "id":"4b840b80-99a8-4817-bc5e-9b33acfc4cbb", "starting_time": 1910, "ending_time": 1915, delay: 5, cost: 0},
	{"label":"Script Task (Sim95)", task: "Script Task", "id":"e7a9f046-70e3-4cb8-af58-bc6f3d986fdc", "starting_time": 1915, "ending_time": 1920, delay: 0, cost: 0},
	{"label":"Service Task (Sim96)", task: "Service Task", "id":"b2581613-30d2-4f42-ad3d-7db3ff40849e", "starting_time": 1920, "ending_time": 1925, delay: 0, cost: 0},
	{"label":"User Task B (Sim96)", task: "User Task B", "id":"06c14764-12f7-497f-aee0-061d46a13895", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"User Task A (Sim96)", task: "User Task A", "id":"2e2c9c21-1ff5-4265-bc26-fa52a5dfbdda", "starting_time": 1930, "ending_time": 1935, delay: 5, cost: 0},
	{"label":"Script Task (Sim96)", task: "Script Task", "id":"68428239-35a1-4bc3-a137-e7e1e0ec211d", "starting_time": 1935, "ending_time": 1940, delay: 0, cost: 0},
	{"label":"Service Task (Sim97)", task: "Service Task", "id":"75bc2814-a4c2-4c46-83d9-ac48a9acbb06", "starting_time": 1940, "ending_time": 1945, delay: 0, cost: 0},
	{"label":"User Task A (Sim97)", task: "User Task A", "id":"bea53caf-b444-474a-b9d4-00feefd3cfb5", "starting_time": 1945, "ending_time": 1950, delay: 0, cost: 0},
	{"label":"User Task B (Sim97)", task: "User Task B", "id":"f593e053-640d-433f-8f1d-dcaaff4a7e9c", "starting_time": 1950, "ending_time": 1955, delay: 5, cost: 0},
	{"label":"Script Task (Sim97)", task: "Script Task", "id":"4be18272-cb4d-4f34-a429-df94e7abb89e", "starting_time": 1955, "ending_time": 1960, delay: 0, cost: 0},
	{"label":"Service Task (Sim98)", task: "Service Task", "id":"b8aaceb6-71de-4b49-9189-11d3fc69a10f", "starting_time": 1960, "ending_time": 1965, delay: 0, cost: 0},
	{"label":"User Task A (Sim98)", task: "User Task A", "id":"049738ed-9a9f-418e-aef9-9f34ca47307c", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"User Task B (Sim98)", task: "User Task B", "id":"798002c6-4485-4b18-a0ea-dac9e6bc7efe", "starting_time": 1970, "ending_time": 1975, delay: 5, cost: 0},
	{"label":"Script Task (Sim98)", task: "Script Task", "id":"dadb542f-3882-4ca3-b8e4-49345e8a03f9", "starting_time": 1975, "ending_time": 1980, delay: 0, cost: 0},
	{"label":"Service Task (Sim99)", task: "Service Task", "id":"295fb2e0-97c0-4ad1-af91-9cf6e62103af", "starting_time": 1980, "ending_time": 1985, delay: 0, cost: 0},
	{"label":"User Task B (Sim99)", task: "User Task B", "id":"f47f4f30-cc8a-48e5-bfd5-5f07e5e87dfd", "starting_time": 1985, "ending_time": 1990, delay: 0, cost: 0},
	{"label":"User Task A (Sim99)", task: "User Task A", "id":"7b01e4ed-aec9-42b3-97ca-ee70d53b1ff1", "starting_time": 1990, "ending_time": 1995, delay: 5, cost: 0},
	{"label":"Script Task (Sim99)", task: "Script Task", "id":"9a700dcc-1eec-493c-b0a4-3b4356b8b7e8", "starting_time": 1995, "ending_time": 2000, delay: 0, cost: 0}
]},
{label: "r2", times: [

]},
{label: "r3", times: [

]},
{label: "r4", times: [

]},
];

var simulationData = [
{label: "Sim0", times: [
	{"label":"Service Task (Sim0)", task: "Service Task", "id":"9aeefe97-d1bc-4b0e-bbf1-567bd345703c", "starting_time": 0, "ending_time": 5, delay: 0, cost: 0},
	{"label":"User Task B (Sim0)", task: "User Task B", "id":"a972af7e-dbcb-4c7e-b536-6975e9ec1ca6", "starting_time": 5, "ending_time": 10, delay: 0, cost: 0},
	{"label":"User Task A (Sim0)", task: "User Task A", "id":"0f22b0fc-5c85-4d76-a1d4-d72672033e63", "starting_time": 10, "ending_time": 15, delay: 5, cost: 0},
	{"label":"Script Task (Sim0)", task: "Script Task", "id":"a907b05c-4141-4485-b4d0-1c2098265fcf", "starting_time": 15, "ending_time": 20, delay: 0, cost: 0}
]},
{label: "Sim1", times: [
	{"label":"Service Task (Sim1)", task: "Service Task", "id":"b73c83d4-179f-4851-aeaf-94dffab28a74", "starting_time": 20, "ending_time": 25, delay: 0, cost: 0},
	{"label":"User Task B (Sim1)", task: "User Task B", "id":"8e138599-bf7f-407a-8aa4-29fb8422cea4", "starting_time": 25, "ending_time": 30, delay: 0, cost: 0},
	{"label":"User Task A (Sim1)", task: "User Task A", "id":"3482bfe0-a45b-42a4-b252-53cd8ac643a1", "starting_time": 30, "ending_time": 35, delay: 5, cost: 0},
	{"label":"Script Task (Sim1)", task: "Script Task", "id":"ecd043a9-5472-4000-ae9e-68ca4a706021", "starting_time": 35, "ending_time": 40, delay: 0, cost: 0}
]},
{label: "Sim10", times: [
	{"label":"Service Task (Sim10)", task: "Service Task", "id":"d08e8b81-4a32-4f82-8ada-5406defce3c3", "starting_time": 200, "ending_time": 205, delay: 0, cost: 0},
	{"label":"User Task B (Sim10)", task: "User Task B", "id":"bc25ae4c-3e55-401d-9359-735afee19f37", "starting_time": 205, "ending_time": 210, delay: 0, cost: 0},
	{"label":"User Task A (Sim10)", task: "User Task A", "id":"3025e3ba-719e-4290-9eaf-00f5344e6eda", "starting_time": 210, "ending_time": 215, delay: 5, cost: 0},
	{"label":"Script Task (Sim10)", task: "Script Task", "id":"48f39070-9d85-4af5-a5a6-3aeb605c1d3a", "starting_time": 215, "ending_time": 220, delay: 0, cost: 0}
]},
{label: "Sim11", times: [
	{"label":"Service Task (Sim11)", task: "Service Task", "id":"c4b3a213-07b1-42ce-b472-34b599896a45", "starting_time": 220, "ending_time": 225, delay: 0, cost: 0},
	{"label":"User Task A (Sim11)", task: "User Task A", "id":"34305813-c57a-4604-82d1-d6b975c6d270", "starting_time": 225, "ending_time": 230, delay: 0, cost: 0},
	{"label":"User Task B (Sim11)", task: "User Task B", "id":"6b7eaa0c-5961-481e-94a0-340c94c23078", "starting_time": 230, "ending_time": 235, delay: 5, cost: 0},
	{"label":"Script Task (Sim11)", task: "Script Task", "id":"92be2c6d-25b0-46c1-8d3f-f90877b29fb1", "starting_time": 235, "ending_time": 240, delay: 0, cost: 0}
]},
{label: "Sim12", times: [
	{"label":"Service Task (Sim12)", task: "Service Task", "id":"2675c2ef-a48e-4139-8203-916114634e8f", "starting_time": 240, "ending_time": 245, delay: 0, cost: 0},
	{"label":"User Task B (Sim12)", task: "User Task B", "id":"5cf2dcef-3eea-47b7-b8e9-e1f2a088b5c0", "starting_time": 245, "ending_time": 250, delay: 0, cost: 0},
	{"label":"User Task A (Sim12)", task: "User Task A", "id":"7f0db2da-37de-452a-9f82-5623bd399f58", "starting_time": 250, "ending_time": 255, delay: 5, cost: 0},
	{"label":"Script Task (Sim12)", task: "Script Task", "id":"2f65d76f-8713-48a3-b1c6-2fb612e247e6", "starting_time": 255, "ending_time": 260, delay: 0, cost: 0}
]},
{label: "Sim13", times: [
	{"label":"Service Task (Sim13)", task: "Service Task", "id":"3ab48890-0317-4bf1-9bc0-200e599b25bd", "starting_time": 260, "ending_time": 265, delay: 0, cost: 0},
	{"label":"User Task A (Sim13)", task: "User Task A", "id":"844fcb77-153b-4069-a539-dd59b530ffc4", "starting_time": 265, "ending_time": 270, delay: 0, cost: 0},
	{"label":"User Task B (Sim13)", task: "User Task B", "id":"1a21263c-8586-454d-bbce-041203f93f9e", "starting_time": 270, "ending_time": 275, delay: 5, cost: 0},
	{"label":"Script Task (Sim13)", task: "Script Task", "id":"dd1e6b0b-6cd6-4895-96b6-1a91c79c7e8e", "starting_time": 275, "ending_time": 280, delay: 0, cost: 0}
]},
{label: "Sim14", times: [
	{"label":"Service Task (Sim14)", task: "Service Task", "id":"5bfe559a-a16d-4b49-8e7d-c5527a963091", "starting_time": 280, "ending_time": 285, delay: 0, cost: 0},
	{"label":"User Task B (Sim14)", task: "User Task B", "id":"e3cfc3ef-5e63-463d-ad4e-c0356c6f1d8c", "starting_time": 285, "ending_time": 290, delay: 0, cost: 0},
	{"label":"User Task A (Sim14)", task: "User Task A", "id":"32933fca-ef9c-41a0-a0e3-c5b85ae35aea", "starting_time": 290, "ending_time": 295, delay: 5, cost: 0},
	{"label":"Script Task (Sim14)", task: "Script Task", "id":"e5438261-3be7-4aa1-aae2-c718fb2dd3fe", "starting_time": 295, "ending_time": 300, delay: 0, cost: 0}
]},
{label: "Sim15", times: [
	{"label":"Service Task (Sim15)", task: "Service Task", "id":"adc7f2e9-423f-48d3-9529-c06dc9ca7eb8", "starting_time": 300, "ending_time": 305, delay: 0, cost: 0},
	{"label":"User Task A (Sim15)", task: "User Task A", "id":"16ead85d-1ed6-4c65-8f98-307bfbc3f3c0", "starting_time": 305, "ending_time": 310, delay: 0, cost: 0},
	{"label":"User Task B (Sim15)", task: "User Task B", "id":"2f287060-8b58-4417-8396-eefdcfd38d0e", "starting_time": 310, "ending_time": 315, delay: 5, cost: 0},
	{"label":"Script Task (Sim15)", task: "Script Task", "id":"435008b3-2d0b-49ea-a3eb-83f6663ad225", "starting_time": 315, "ending_time": 320, delay: 0, cost: 0}
]},
{label: "Sim16", times: [
	{"label":"Service Task (Sim16)", task: "Service Task", "id":"aeed69ed-8dab-400c-a760-95f03b72d233", "starting_time": 320, "ending_time": 325, delay: 0, cost: 0},
	{"label":"User Task B (Sim16)", task: "User Task B", "id":"e693db44-11f9-4213-a380-5fb5eeff74e6", "starting_time": 325, "ending_time": 330, delay: 0, cost: 0},
	{"label":"User Task A (Sim16)", task: "User Task A", "id":"7aadc2fd-5a70-483d-9328-4c315b6630d4", "starting_time": 330, "ending_time": 335, delay: 5, cost: 0},
	{"label":"Script Task (Sim16)", task: "Script Task", "id":"a083b002-f038-4851-a695-c497213eb449", "starting_time": 335, "ending_time": 340, delay: 0, cost: 0}
]},
{label: "Sim17", times: [
	{"label":"Service Task (Sim17)", task: "Service Task", "id":"afb7851d-a472-4b45-b03b-e9e7d3f0f444", "starting_time": 340, "ending_time": 345, delay: 0, cost: 0},
	{"label":"User Task B (Sim17)", task: "User Task B", "id":"f1e2b102-9262-48e1-80e9-6182d2ce5129", "starting_time": 345, "ending_time": 350, delay: 0, cost: 0},
	{"label":"User Task A (Sim17)", task: "User Task A", "id":"7903a7c0-8c26-4305-ae70-daa3da56d38e", "starting_time": 350, "ending_time": 355, delay: 5, cost: 0},
	{"label":"Script Task (Sim17)", task: "Script Task", "id":"470a5c67-297e-4447-8bde-3f80d3d76087", "starting_time": 355, "ending_time": 360, delay: 0, cost: 0}
]},
{label: "Sim18", times: [
	{"label":"Service Task (Sim18)", task: "Service Task", "id":"8c1bf011-c443-48ec-bdba-67e939b73d09", "starting_time": 360, "ending_time": 365, delay: 0, cost: 0},
	{"label":"User Task B (Sim18)", task: "User Task B", "id":"cdadd5a5-0039-4de6-8201-74d6edcf937a", "starting_time": 365, "ending_time": 370, delay: 0, cost: 0},
	{"label":"User Task A (Sim18)", task: "User Task A", "id":"54952bc3-5027-468b-9ac7-1909afae51b0", "starting_time": 370, "ending_time": 375, delay: 5, cost: 0},
	{"label":"Script Task (Sim18)", task: "Script Task", "id":"4489d7a9-6689-47c7-8985-01f104149fcb", "starting_time": 375, "ending_time": 380, delay: 0, cost: 0}
]},
{label: "Sim19", times: [
	{"label":"Service Task (Sim19)", task: "Service Task", "id":"51f58c36-983d-471a-9287-db2d9213b19f", "starting_time": 380, "ending_time": 385, delay: 0, cost: 0},
	{"label":"User Task B (Sim19)", task: "User Task B", "id":"a6f30bb6-1bbe-4cb6-a7ca-b1a79b2f3a5a", "starting_time": 385, "ending_time": 390, delay: 0, cost: 0},
	{"label":"User Task A (Sim19)", task: "User Task A", "id":"f569f45f-b922-469d-91b4-8f5f18662503", "starting_time": 390, "ending_time": 395, delay: 5, cost: 0},
	{"label":"Script Task (Sim19)", task: "Script Task", "id":"1b4ad041-939c-4766-8288-5cdd3695e9c9", "starting_time": 395, "ending_time": 400, delay: 0, cost: 0}
]},
{label: "Sim2", times: [
	{"label":"Service Task (Sim2)", task: "Service Task", "id":"d1751d07-60d3-4d98-bc44-0f37537fd8c1", "starting_time": 40, "ending_time": 45, delay: 0, cost: 0},
	{"label":"User Task A (Sim2)", task: "User Task A", "id":"b3009dd3-05a4-4a62-aef8-7c39bb4fe847", "starting_time": 45, "ending_time": 50, delay: 0, cost: 0},
	{"label":"User Task B (Sim2)", task: "User Task B", "id":"b48d9986-fb00-460f-b117-a7821d6e5532", "starting_time": 50, "ending_time": 55, delay: 5, cost: 0},
	{"label":"Script Task (Sim2)", task: "Script Task", "id":"169c8506-8c9e-42f2-828d-6cbe3abf9321", "starting_time": 55, "ending_time": 60, delay: 0, cost: 0}
]},
{label: "Sim20", times: [
	{"label":"Service Task (Sim20)", task: "Service Task", "id":"29dd7e6b-72f4-4c5e-93a0-18d929042cc7", "starting_time": 400, "ending_time": 405, delay: 0, cost: 0},
	{"label":"User Task B (Sim20)", task: "User Task B", "id":"b72a2849-50a7-4b00-89e2-78ffc42c02b5", "starting_time": 405, "ending_time": 410, delay: 0, cost: 0},
	{"label":"User Task A (Sim20)", task: "User Task A", "id":"fe4b0a3d-1125-49fb-af2d-e7e49beaafc2", "starting_time": 410, "ending_time": 415, delay: 5, cost: 0},
	{"label":"Script Task (Sim20)", task: "Script Task", "id":"51ccdac9-f76e-4d51-85c9-21dd6453cfc1", "starting_time": 415, "ending_time": 420, delay: 0, cost: 0}
]},
{label: "Sim21", times: [
	{"label":"Service Task (Sim21)", task: "Service Task", "id":"07cf7f14-0f5e-4390-a132-a5cbd9f82c20", "starting_time": 420, "ending_time": 425, delay: 0, cost: 0},
	{"label":"User Task B (Sim21)", task: "User Task B", "id":"f597b297-c037-4534-b8ba-e63bc2736fcf", "starting_time": 425, "ending_time": 430, delay: 0, cost: 0},
	{"label":"User Task A (Sim21)", task: "User Task A", "id":"024f96ef-8630-4ef2-97fd-ad0816b06dc2", "starting_time": 430, "ending_time": 435, delay: 5, cost: 0},
	{"label":"Script Task (Sim21)", task: "Script Task", "id":"e8fad3c6-468c-488a-97cc-95113b5c4b48", "starting_time": 435, "ending_time": 440, delay: 0, cost: 0}
]},
{label: "Sim22", times: [
	{"label":"Service Task (Sim22)", task: "Service Task", "id":"a1b91eb6-7292-4e45-9ffb-6c330fe69b88", "starting_time": 440, "ending_time": 445, delay: 0, cost: 0},
	{"label":"User Task A (Sim22)", task: "User Task A", "id":"f1803885-62c2-4701-9583-3684cb547b89", "starting_time": 445, "ending_time": 450, delay: 0, cost: 0},
	{"label":"User Task B (Sim22)", task: "User Task B", "id":"79f96d3b-7f2d-406a-9014-cca437611588", "starting_time": 450, "ending_time": 455, delay: 5, cost: 0},
	{"label":"Script Task (Sim22)", task: "Script Task", "id":"b0c45086-1b7f-46a5-a6f9-7bcbd3fc8c92", "starting_time": 455, "ending_time": 460, delay: 0, cost: 0}
]},
{label: "Sim23", times: [
	{"label":"Service Task (Sim23)", task: "Service Task", "id":"9e54bd85-ce87-4428-a330-67a32df72f09", "starting_time": 460, "ending_time": 465, delay: 0, cost: 0},
	{"label":"User Task B (Sim23)", task: "User Task B", "id":"a56ba3f2-b2e3-48e3-bf4a-cd3c3bb4f008", "starting_time": 465, "ending_time": 470, delay: 0, cost: 0},
	{"label":"User Task A (Sim23)", task: "User Task A", "id":"5187ab08-66cf-41f2-a09e-555a0e8af82c", "starting_time": 470, "ending_time": 475, delay: 5, cost: 0},
	{"label":"Script Task (Sim23)", task: "Script Task", "id":"f44e1666-0251-4296-a361-053b70b1acbe", "starting_time": 475, "ending_time": 480, delay: 0, cost: 0}
]},
{label: "Sim24", times: [
	{"label":"Service Task (Sim24)", task: "Service Task", "id":"cac27fb0-86e7-4244-a37f-c08346556d95", "starting_time": 480, "ending_time": 485, delay: 0, cost: 0},
	{"label":"User Task B (Sim24)", task: "User Task B", "id":"d035e918-46cc-48c0-8f98-2c36ee410c46", "starting_time": 485, "ending_time": 490, delay: 0, cost: 0},
	{"label":"User Task A (Sim24)", task: "User Task A", "id":"6c504f44-27b2-42f6-ba4c-67a0a46719d1", "starting_time": 490, "ending_time": 495, delay: 5, cost: 0},
	{"label":"Script Task (Sim24)", task: "Script Task", "id":"1cfa9f1b-af26-4265-b141-469352aeb806", "starting_time": 495, "ending_time": 500, delay: 0, cost: 0}
]},
{label: "Sim25", times: [
	{"label":"Service Task (Sim25)", task: "Service Task", "id":"408834e9-9651-44f8-9561-3835b864fa85", "starting_time": 500, "ending_time": 505, delay: 0, cost: 0},
	{"label":"User Task A (Sim25)", task: "User Task A", "id":"c721b774-0ce1-4ab4-bc31-e25e7b921a5c", "starting_time": 505, "ending_time": 510, delay: 0, cost: 0},
	{"label":"User Task B (Sim25)", task: "User Task B", "id":"6168a45a-05a2-40de-9244-4ecdd03ea6ec", "starting_time": 510, "ending_time": 515, delay: 5, cost: 0},
	{"label":"Script Task (Sim25)", task: "Script Task", "id":"54323aad-8246-4463-b59b-f5d31299a929", "starting_time": 515, "ending_time": 520, delay: 0, cost: 0}
]},
{label: "Sim26", times: [
	{"label":"Service Task (Sim26)", task: "Service Task", "id":"880aad7d-1c8d-4e2e-aff7-3c841ee2a0a9", "starting_time": 520, "ending_time": 525, delay: 0, cost: 0},
	{"label":"User Task A (Sim26)", task: "User Task A", "id":"47cff48f-beda-4a6a-95dc-c1343f9bd8d4", "starting_time": 525, "ending_time": 530, delay: 0, cost: 0},
	{"label":"User Task B (Sim26)", task: "User Task B", "id":"556150cf-096b-41e7-93ad-7352b96259bb", "starting_time": 530, "ending_time": 535, delay: 5, cost: 0},
	{"label":"Script Task (Sim26)", task: "Script Task", "id":"0cc0e1fe-4f33-4623-b893-af30dee0b06c", "starting_time": 535, "ending_time": 540, delay: 0, cost: 0}
]},
{label: "Sim27", times: [
	{"label":"Service Task (Sim27)", task: "Service Task", "id":"b9d612d4-6d78-4ac9-ad81-f6f8850349e7", "starting_time": 540, "ending_time": 545, delay: 0, cost: 0},
	{"label":"User Task B (Sim27)", task: "User Task B", "id":"bee4a5a5-5877-46d5-bf6a-b341429747c6", "starting_time": 545, "ending_time": 550, delay: 0, cost: 0},
	{"label":"User Task A (Sim27)", task: "User Task A", "id":"32dd742a-cdb6-4184-995e-6f744c40040b", "starting_time": 550, "ending_time": 555, delay: 5, cost: 0},
	{"label":"Script Task (Sim27)", task: "Script Task", "id":"17cd5b98-f49a-488d-8a43-e315efc2cd9f", "starting_time": 555, "ending_time": 560, delay: 0, cost: 0}
]},
{label: "Sim28", times: [
	{"label":"Service Task (Sim28)", task: "Service Task", "id":"d34003e5-c6ab-4c67-9b1c-ef58f0fbcb41", "starting_time": 560, "ending_time": 565, delay: 0, cost: 0},
	{"label":"User Task A (Sim28)", task: "User Task A", "id":"bd78b4da-721f-4de9-8516-0e6ba4f50572", "starting_time": 565, "ending_time": 570, delay: 0, cost: 0},
	{"label":"User Task B (Sim28)", task: "User Task B", "id":"c233458b-75bd-44ba-9eee-edd3ad98221c", "starting_time": 570, "ending_time": 575, delay: 5, cost: 0},
	{"label":"Script Task (Sim28)", task: "Script Task", "id":"df40f3d2-ff0d-49c6-aefd-11e23c9edcfa", "starting_time": 575, "ending_time": 580, delay: 0, cost: 0}
]},
{label: "Sim29", times: [
	{"label":"Service Task (Sim29)", task: "Service Task", "id":"b63940e9-732d-4acc-9191-1517f3677906", "starting_time": 580, "ending_time": 585, delay: 0, cost: 0},
	{"label":"User Task B (Sim29)", task: "User Task B", "id":"ac4677ad-bed2-4004-bd34-af390a5dc08d", "starting_time": 585, "ending_time": 590, delay: 0, cost: 0},
	{"label":"User Task A (Sim29)", task: "User Task A", "id":"5d696e5e-5a19-4615-85b4-73893e12b22f", "starting_time": 590, "ending_time": 595, delay: 5, cost: 0},
	{"label":"Script Task (Sim29)", task: "Script Task", "id":"a6e3efdb-09a4-4830-98af-860871b8cbba", "starting_time": 595, "ending_time": 600, delay: 0, cost: 0}
]},
{label: "Sim3", times: [
	{"label":"Service Task (Sim3)", task: "Service Task", "id":"a88b59b4-64c8-46c3-ad43-964049d5bbef", "starting_time": 60, "ending_time": 65, delay: 0, cost: 0},
	{"label":"User Task B (Sim3)", task: "User Task B", "id":"e411de9a-6315-457b-bec6-003194db937c", "starting_time": 65, "ending_time": 70, delay: 0, cost: 0},
	{"label":"User Task A (Sim3)", task: "User Task A", "id":"e5cb542d-e35e-4356-b81c-5837038a6eac", "starting_time": 70, "ending_time": 75, delay: 5, cost: 0},
	{"label":"Script Task (Sim3)", task: "Script Task", "id":"c07116c8-8fd6-4191-a380-87f55abc269b", "starting_time": 75, "ending_time": 80, delay: 0, cost: 0}
]},
{label: "Sim30", times: [
	{"label":"Service Task (Sim30)", task: "Service Task", "id":"88e742b8-fac6-429e-8852-67bcf4ad480d", "starting_time": 600, "ending_time": 605, delay: 0, cost: 0},
	{"label":"User Task B (Sim30)", task: "User Task B", "id":"475ce0bd-a6ae-4b21-96a4-337c9eacdedc", "starting_time": 605, "ending_time": 610, delay: 0, cost: 0},
	{"label":"User Task A (Sim30)", task: "User Task A", "id":"62b19b48-2cc7-463c-ae6b-dcc27e809097", "starting_time": 610, "ending_time": 615, delay: 5, cost: 0},
	{"label":"Script Task (Sim30)", task: "Script Task", "id":"d7983d73-6ca4-4605-ab41-61c005278956", "starting_time": 615, "ending_time": 620, delay: 0, cost: 0}
]},
{label: "Sim31", times: [
	{"label":"Service Task (Sim31)", task: "Service Task", "id":"54dc27d1-3259-41f1-99d9-c6be53f24d9f", "starting_time": 620, "ending_time": 625, delay: 0, cost: 0},
	{"label":"User Task B (Sim31)", task: "User Task B", "id":"a1ecf88c-2f0a-4274-81f5-4cfeb5ea5df1", "starting_time": 625, "ending_time": 630, delay: 0, cost: 0},
	{"label":"User Task A (Sim31)", task: "User Task A", "id":"ee63f6ae-21e6-4c6e-91e5-738fc9df15b6", "starting_time": 630, "ending_time": 635, delay: 5, cost: 0},
	{"label":"Script Task (Sim31)", task: "Script Task", "id":"fb9ea121-05dc-4a7e-a40a-849167df5ddf", "starting_time": 635, "ending_time": 640, delay: 0, cost: 0}
]},
{label: "Sim32", times: [
	{"label":"Service Task (Sim32)", task: "Service Task", "id":"2e53bf21-f606-4daf-b6a6-9d88cbfea503", "starting_time": 640, "ending_time": 645, delay: 0, cost: 0},
	{"label":"User Task B (Sim32)", task: "User Task B", "id":"8c13151a-4ebb-43eb-ae99-9fd1eef38467", "starting_time": 645, "ending_time": 650, delay: 0, cost: 0},
	{"label":"User Task A (Sim32)", task: "User Task A", "id":"bd66f9c5-527f-40f3-a4c5-8ce0a67ab3f6", "starting_time": 650, "ending_time": 655, delay: 5, cost: 0},
	{"label":"Script Task (Sim32)", task: "Script Task", "id":"9158c8ae-8ce9-467d-9507-52c2e44f324d", "starting_time": 655, "ending_time": 660, delay: 0, cost: 0}
]},
{label: "Sim33", times: [
	{"label":"Service Task (Sim33)", task: "Service Task", "id":"50be62c5-dfb7-45a7-98ef-cc87ce09abec", "starting_time": 660, "ending_time": 665, delay: 0, cost: 0},
	{"label":"User Task A (Sim33)", task: "User Task A", "id":"836cb8dc-dfd8-43cd-80c1-40d86a18bd5f", "starting_time": 665, "ending_time": 670, delay: 0, cost: 0},
	{"label":"User Task B (Sim33)", task: "User Task B", "id":"861257f0-1549-4fc2-868b-da77527399c5", "starting_time": 670, "ending_time": 675, delay: 5, cost: 0},
	{"label":"Script Task (Sim33)", task: "Script Task", "id":"c4efef07-5f64-4a66-b908-82c0d81152fc", "starting_time": 675, "ending_time": 680, delay: 0, cost: 0}
]},
{label: "Sim34", times: [
	{"label":"Service Task (Sim34)", task: "Service Task", "id":"66734c21-27df-481f-ac8c-f53fa6188927", "starting_time": 680, "ending_time": 685, delay: 0, cost: 0},
	{"label":"User Task A (Sim34)", task: "User Task A", "id":"964578c9-adab-4a67-828b-deec67e66912", "starting_time": 685, "ending_time": 690, delay: 0, cost: 0},
	{"label":"User Task B (Sim34)", task: "User Task B", "id":"ac39e52c-5b27-40c5-8854-0e77a0930373", "starting_time": 690, "ending_time": 695, delay: 5, cost: 0},
	{"label":"Script Task (Sim34)", task: "Script Task", "id":"9a2a03b8-36ec-40a3-a827-70641e878772", "starting_time": 695, "ending_time": 700, delay: 0, cost: 0}
]},
{label: "Sim35", times: [
	{"label":"Service Task (Sim35)", task: "Service Task", "id":"40f22aff-c57b-4e51-a8ff-4de4a68289ee", "starting_time": 700, "ending_time": 705, delay: 0, cost: 0},
	{"label":"User Task B (Sim35)", task: "User Task B", "id":"a2dec0df-e745-45f0-adbc-e710a8d00b8f", "starting_time": 705, "ending_time": 710, delay: 0, cost: 0},
	{"label":"User Task A (Sim35)", task: "User Task A", "id":"7c02c57c-5311-4959-a547-9e46f6af931d", "starting_time": 710, "ending_time": 715, delay: 5, cost: 0},
	{"label":"Script Task (Sim35)", task: "Script Task", "id":"b19d3e3c-69d4-45d3-a275-344656461556", "starting_time": 715, "ending_time": 720, delay: 0, cost: 0}
]},
{label: "Sim36", times: [
	{"label":"Service Task (Sim36)", task: "Service Task", "id":"4c952b9c-09c0-4bc8-9ede-8c8ecc7d6da9", "starting_time": 720, "ending_time": 725, delay: 0, cost: 0},
	{"label":"User Task B (Sim36)", task: "User Task B", "id":"a471bc2d-e06e-44f7-ae92-c779dc622e55", "starting_time": 725, "ending_time": 730, delay: 0, cost: 0},
	{"label":"User Task A (Sim36)", task: "User Task A", "id":"c321530c-3693-4366-9b44-f280e3b8919a", "starting_time": 730, "ending_time": 735, delay: 5, cost: 0},
	{"label":"Script Task (Sim36)", task: "Script Task", "id":"9e70c758-1346-419c-871c-76fb6a989877", "starting_time": 735, "ending_time": 740, delay: 0, cost: 0}
]},
{label: "Sim37", times: [
	{"label":"Service Task (Sim37)", task: "Service Task", "id":"a7d3c64c-224e-441a-ac9e-7f8942ad9a77", "starting_time": 740, "ending_time": 745, delay: 0, cost: 0},
	{"label":"User Task A (Sim37)", task: "User Task A", "id":"e9c6624c-da9a-4317-b36c-15ea65e53e82", "starting_time": 745, "ending_time": 750, delay: 0, cost: 0},
	{"label":"User Task B (Sim37)", task: "User Task B", "id":"1334ce7e-cddb-456e-a3a4-d5f61148d751", "starting_time": 750, "ending_time": 755, delay: 5, cost: 0},
	{"label":"Script Task (Sim37)", task: "Script Task", "id":"b48212e3-2f3c-45ef-87b5-fc1f2b41a46b", "starting_time": 755, "ending_time": 760, delay: 0, cost: 0}
]},
{label: "Sim38", times: [
	{"label":"Service Task (Sim38)", task: "Service Task", "id":"8e332ee7-cb68-4a44-9b7c-2829cfb640ef", "starting_time": 760, "ending_time": 765, delay: 0, cost: 0},
	{"label":"User Task A (Sim38)", task: "User Task A", "id":"ec44f6a4-fc93-47a8-af6c-85bc9ad04c14", "starting_time": 765, "ending_time": 770, delay: 0, cost: 0},
	{"label":"User Task B (Sim38)", task: "User Task B", "id":"367dd570-64f1-486e-8aff-36ba81b8ee44", "starting_time": 770, "ending_time": 775, delay: 5, cost: 0},
	{"label":"Script Task (Sim38)", task: "Script Task", "id":"b3b3a414-2f10-4c87-9edb-a1b18161a6bf", "starting_time": 775, "ending_time": 780, delay: 0, cost: 0}
]},
{label: "Sim39", times: [
	{"label":"Service Task (Sim39)", task: "Service Task", "id":"02f2a21d-1a91-4425-aea3-ae211f6e6847", "starting_time": 780, "ending_time": 785, delay: 0, cost: 0},
	{"label":"User Task A (Sim39)", task: "User Task A", "id":"3c32b347-2b71-4308-9234-fe0e6149cbfd", "starting_time": 785, "ending_time": 790, delay: 0, cost: 0},
	{"label":"User Task B (Sim39)", task: "User Task B", "id":"531b3658-da18-417e-9119-ab2fe1d8751f", "starting_time": 790, "ending_time": 795, delay: 5, cost: 0},
	{"label":"Script Task (Sim39)", task: "Script Task", "id":"d7563ba9-c9f7-43f6-a839-778a238fe1d0", "starting_time": 795, "ending_time": 800, delay: 0, cost: 0}
]},
{label: "Sim4", times: [
	{"label":"Service Task (Sim4)", task: "Service Task", "id":"2128bdd0-6abb-4b1a-a8ba-537ed08d3bcd", "starting_time": 80, "ending_time": 85, delay: 0, cost: 0},
	{"label":"User Task B (Sim4)", task: "User Task B", "id":"862f8664-ca10-40a4-a742-2be7c6b6bb7b", "starting_time": 85, "ending_time": 90, delay: 0, cost: 0},
	{"label":"User Task A (Sim4)", task: "User Task A", "id":"2b0d5c0c-efe0-4907-a17e-84956e1325fc", "starting_time": 90, "ending_time": 95, delay: 5, cost: 0},
	{"label":"Script Task (Sim4)", task: "Script Task", "id":"3745472b-59bf-477d-9f9f-d6884fcde0b4", "starting_time": 95, "ending_time": 100, delay: 0, cost: 0}
]},
{label: "Sim40", times: [
	{"label":"Service Task (Sim40)", task: "Service Task", "id":"bab99573-00a8-41e6-adde-f57eed1eac53", "starting_time": 800, "ending_time": 805, delay: 0, cost: 0},
	{"label":"User Task B (Sim40)", task: "User Task B", "id":"c6951db9-b381-48f4-bedf-5a72329193be", "starting_time": 805, "ending_time": 810, delay: 0, cost: 0},
	{"label":"User Task A (Sim40)", task: "User Task A", "id":"09d09b0e-e167-4aec-88b1-336a01821791", "starting_time": 810, "ending_time": 815, delay: 5, cost: 0},
	{"label":"Script Task (Sim40)", task: "Script Task", "id":"61f9f5cc-f6ac-4eeb-9ddd-68f19b81f343", "starting_time": 815, "ending_time": 820, delay: 0, cost: 0}
]},
{label: "Sim41", times: [
	{"label":"Service Task (Sim41)", task: "Service Task", "id":"c97afd73-78ab-462b-adc2-468933ff2959", "starting_time": 820, "ending_time": 825, delay: 0, cost: 0},
	{"label":"User Task A (Sim41)", task: "User Task A", "id":"83d70f61-6d74-47bf-8293-b475fdd40be4", "starting_time": 825, "ending_time": 830, delay: 0, cost: 0},
	{"label":"User Task B (Sim41)", task: "User Task B", "id":"0cb88001-8e5f-4ce7-9e26-688b8214ea14", "starting_time": 830, "ending_time": 835, delay: 5, cost: 0},
	{"label":"Script Task (Sim41)", task: "Script Task", "id":"38f26e99-0a65-4a86-ae32-bf0d9fed461e", "starting_time": 835, "ending_time": 840, delay: 0, cost: 0}
]},
{label: "Sim42", times: [
	{"label":"Service Task (Sim42)", task: "Service Task", "id":"357496bf-15fa-4557-9409-c4a796f75dbe", "starting_time": 840, "ending_time": 845, delay: 0, cost: 0},
	{"label":"User Task B (Sim42)", task: "User Task B", "id":"97820507-88d9-47fb-adbc-205425a83b3b", "starting_time": 845, "ending_time": 850, delay: 0, cost: 0},
	{"label":"User Task A (Sim42)", task: "User Task A", "id":"a12eacce-09e0-436e-a227-4fc1adb7741b", "starting_time": 850, "ending_time": 855, delay: 5, cost: 0},
	{"label":"Script Task (Sim42)", task: "Script Task", "id":"10f50555-610d-4068-8de4-c2491d12808b", "starting_time": 855, "ending_time": 860, delay: 0, cost: 0}
]},
{label: "Sim43", times: [
	{"label":"Service Task (Sim43)", task: "Service Task", "id":"ce4c5034-b262-433a-ac18-8be7efb494ae", "starting_time": 860, "ending_time": 865, delay: 0, cost: 0},
	{"label":"User Task A (Sim43)", task: "User Task A", "id":"cbb732d1-d6ed-401b-90fb-abbec4b0f143", "starting_time": 865, "ending_time": 870, delay: 0, cost: 0},
	{"label":"User Task B (Sim43)", task: "User Task B", "id":"e145f7be-a4c2-4e04-b872-df5d952ad6cc", "starting_time": 870, "ending_time": 875, delay: 5, cost: 0},
	{"label":"Script Task (Sim43)", task: "Script Task", "id":"9e22a073-1671-469b-8fc3-4b630eb01801", "starting_time": 875, "ending_time": 880, delay: 0, cost: 0}
]},
{label: "Sim44", times: [
	{"label":"Service Task (Sim44)", task: "Service Task", "id":"e149c2b4-83bb-452c-b3bb-af946e68b1e0", "starting_time": 880, "ending_time": 885, delay: 0, cost: 0},
	{"label":"User Task B (Sim44)", task: "User Task B", "id":"954c05b8-a23e-4c73-8056-f29f92dd465b", "starting_time": 885, "ending_time": 890, delay: 0, cost: 0},
	{"label":"User Task A (Sim44)", task: "User Task A", "id":"711a32b2-ae31-4fba-8cb9-39694faebdc4", "starting_time": 890, "ending_time": 895, delay: 5, cost: 0},
	{"label":"Script Task (Sim44)", task: "Script Task", "id":"b81a9b88-e97a-4171-b997-616f3468419c", "starting_time": 895, "ending_time": 900, delay: 0, cost: 0}
]},
{label: "Sim45", times: [
	{"label":"Service Task (Sim45)", task: "Service Task", "id":"59f59210-57e0-4455-b14a-74da9ffeba97", "starting_time": 900, "ending_time": 905, delay: 0, cost: 0},
	{"label":"User Task A (Sim45)", task: "User Task A", "id":"13ac341f-0bf0-48fc-b1cd-cd18a47c30c1", "starting_time": 905, "ending_time": 910, delay: 0, cost: 0},
	{"label":"User Task B (Sim45)", task: "User Task B", "id":"2a30b0ca-0c2c-49e7-a903-37c52ade389f", "starting_time": 910, "ending_time": 915, delay: 5, cost: 0},
	{"label":"Script Task (Sim45)", task: "Script Task", "id":"dd0fab8c-876b-48f5-a608-4a1f555893d1", "starting_time": 915, "ending_time": 920, delay: 0, cost: 0}
]},
{label: "Sim46", times: [
	{"label":"Service Task (Sim46)", task: "Service Task", "id":"0301526f-7c37-45ab-80ed-bbfae5a3a484", "starting_time": 920, "ending_time": 925, delay: 0, cost: 0},
	{"label":"User Task B (Sim46)", task: "User Task B", "id":"3aef0db9-d83f-49fc-a5a4-09b149170471", "starting_time": 925, "ending_time": 930, delay: 0, cost: 0},
	{"label":"User Task A (Sim46)", task: "User Task A", "id":"53594a9f-e061-41ce-a1d7-134140282ad6", "starting_time": 930, "ending_time": 935, delay: 5, cost: 0},
	{"label":"Script Task (Sim46)", task: "Script Task", "id":"49d6aeec-dc20-48cf-bfda-d385381a63c3", "starting_time": 935, "ending_time": 940, delay: 0, cost: 0}
]},
{label: "Sim47", times: [
	{"label":"Service Task (Sim47)", task: "Service Task", "id":"11717de1-8eab-455b-8be1-0b5cadbaa06d", "starting_time": 940, "ending_time": 945, delay: 0, cost: 0},
	{"label":"User Task B (Sim47)", task: "User Task B", "id":"edf6a7ba-f0c0-4350-a178-7c3d12dae8f0", "starting_time": 945, "ending_time": 950, delay: 0, cost: 0},
	{"label":"User Task A (Sim47)", task: "User Task A", "id":"74032211-681a-4b66-bd25-ea6c0fe46a4c", "starting_time": 950, "ending_time": 955, delay: 5, cost: 0},
	{"label":"Script Task (Sim47)", task: "Script Task", "id":"2ba8ea89-31ef-4e24-9bf7-411b276f96bb", "starting_time": 955, "ending_time": 960, delay: 0, cost: 0}
]},
{label: "Sim48", times: [
	{"label":"Service Task (Sim48)", task: "Service Task", "id":"f8f4dd83-9980-48f8-8e83-6a37b539e603", "starting_time": 960, "ending_time": 965, delay: 0, cost: 0},
	{"label":"User Task A (Sim48)", task: "User Task A", "id":"fd13c04d-1e0d-4440-b3dd-0107c2c28391", "starting_time": 965, "ending_time": 970, delay: 0, cost: 0},
	{"label":"User Task B (Sim48)", task: "User Task B", "id":"2c25e972-4642-4e41-ae73-49591fb8f1b9", "starting_time": 970, "ending_time": 975, delay: 5, cost: 0},
	{"label":"Script Task (Sim48)", task: "Script Task", "id":"faadc45c-d34c-4f8b-9041-898893ac29b7", "starting_time": 975, "ending_time": 980, delay: 0, cost: 0}
]},
{label: "Sim49", times: [
	{"label":"Service Task (Sim49)", task: "Service Task", "id":"aca8b9d1-8fac-446a-b280-47b51c175490", "starting_time": 980, "ending_time": 985, delay: 0, cost: 0},
	{"label":"User Task B (Sim49)", task: "User Task B", "id":"8c64a4c5-cc37-40f7-a9db-d72fc9a829bf", "starting_time": 985, "ending_time": 990, delay: 0, cost: 0},
	{"label":"User Task A (Sim49)", task: "User Task A", "id":"347056cc-4414-4e64-9e6d-c0f5fe85d82f", "starting_time": 990, "ending_time": 995, delay: 5, cost: 0},
	{"label":"Script Task (Sim49)", task: "Script Task", "id":"70e12bb4-f9d4-41da-bfbc-67ced2c61e99", "starting_time": 995, "ending_time": 1000, delay: 0, cost: 0}
]},
{label: "Sim5", times: [
	{"label":"Service Task (Sim5)", task: "Service Task", "id":"c5d1174e-6674-4e34-a2c9-5a6c15935c6e", "starting_time": 100, "ending_time": 105, delay: 0, cost: 0},
	{"label":"User Task B (Sim5)", task: "User Task B", "id":"fdd3a626-6b79-4481-899f-40629c7756b7", "starting_time": 105, "ending_time": 110, delay: 0, cost: 0},
	{"label":"User Task A (Sim5)", task: "User Task A", "id":"fff118dd-5b26-447f-b600-8186a5ba6a9f", "starting_time": 110, "ending_time": 115, delay: 5, cost: 0},
	{"label":"Script Task (Sim5)", task: "Script Task", "id":"50517f86-870d-49ba-a2d5-4e63dd5d50c2", "starting_time": 115, "ending_time": 120, delay: 0, cost: 0}
]},
{label: "Sim50", times: [
	{"label":"Service Task (Sim50)", task: "Service Task", "id":"ecfb7f53-f7df-4ccf-920c-6b54bf07f99a", "starting_time": 1000, "ending_time": 1005, delay: 0, cost: 0},
	{"label":"User Task B (Sim50)", task: "User Task B", "id":"e6920d66-6a01-42dc-a1a7-a52816105b97", "starting_time": 1005, "ending_time": 1010, delay: 0, cost: 0},
	{"label":"User Task A (Sim50)", task: "User Task A", "id":"e7a52ceb-e6cc-4a59-9b6f-27713b738a66", "starting_time": 1010, "ending_time": 1015, delay: 5, cost: 0},
	{"label":"Script Task (Sim50)", task: "Script Task", "id":"09bd0f09-6bf5-43e3-9a67-f6f90311ddff", "starting_time": 1015, "ending_time": 1020, delay: 0, cost: 0}
]},
{label: "Sim51", times: [
	{"label":"Service Task (Sim51)", task: "Service Task", "id":"6888c24c-6086-4b9e-a657-c9fc8ce3a484", "starting_time": 1020, "ending_time": 1025, delay: 0, cost: 0},
	{"label":"User Task A (Sim51)", task: "User Task A", "id":"b9fe6fac-69db-4885-b45f-abe90f24b4b8", "starting_time": 1025, "ending_time": 1030, delay: 0, cost: 0},
	{"label":"User Task B (Sim51)", task: "User Task B", "id":"2fa00185-6e84-4fff-a334-fba2d668d1b1", "starting_time": 1030, "ending_time": 1035, delay: 5, cost: 0},
	{"label":"Script Task (Sim51)", task: "Script Task", "id":"c3c8b332-6e87-4c17-b17a-dcd2f628093e", "starting_time": 1035, "ending_time": 1040, delay: 0, cost: 0}
]},
{label: "Sim52", times: [
	{"label":"Service Task (Sim52)", task: "Service Task", "id":"edb9e7c9-9d62-4d81-b215-4a4622672b99", "starting_time": 1040, "ending_time": 1045, delay: 0, cost: 0},
	{"label":"User Task A (Sim52)", task: "User Task A", "id":"e7cdd299-ecc1-4996-bc1d-eb37354379b5", "starting_time": 1045, "ending_time": 1050, delay: 0, cost: 0},
	{"label":"User Task B (Sim52)", task: "User Task B", "id":"63372ff2-eed8-406d-b7d1-d608d6aa39a2", "starting_time": 1050, "ending_time": 1055, delay: 5, cost: 0},
	{"label":"Script Task (Sim52)", task: "Script Task", "id":"a136cea1-da1b-4988-8f18-6414b9123e53", "starting_time": 1055, "ending_time": 1060, delay: 0, cost: 0}
]},
{label: "Sim53", times: [
	{"label":"Service Task (Sim53)", task: "Service Task", "id":"89ba5997-737f-4c62-b94a-debe530184e6", "starting_time": 1060, "ending_time": 1065, delay: 0, cost: 0},
	{"label":"User Task B (Sim53)", task: "User Task B", "id":"1bac2037-e2fc-408f-9e97-997a7fb821da", "starting_time": 1065, "ending_time": 1070, delay: 0, cost: 0},
	{"label":"User Task A (Sim53)", task: "User Task A", "id":"3a0bffe3-d89e-4186-a060-fac1240863ac", "starting_time": 1070, "ending_time": 1075, delay: 5, cost: 0},
	{"label":"Script Task (Sim53)", task: "Script Task", "id":"32290e6a-451e-4382-b745-c5df9030fc80", "starting_time": 1075, "ending_time": 1080, delay: 0, cost: 0}
]},
{label: "Sim54", times: [
	{"label":"Service Task (Sim54)", task: "Service Task", "id":"64522237-cecd-4ce4-bc74-2b95d3b0151c", "starting_time": 1080, "ending_time": 1085, delay: 0, cost: 0},
	{"label":"User Task B (Sim54)", task: "User Task B", "id":"84d563bc-047b-41a9-a6e2-4edd0a157c30", "starting_time": 1085, "ending_time": 1090, delay: 0, cost: 0},
	{"label":"User Task A (Sim54)", task: "User Task A", "id":"9d32e1f0-6d99-43ac-964b-ac5599a7c58c", "starting_time": 1090, "ending_time": 1095, delay: 5, cost: 0},
	{"label":"Script Task (Sim54)", task: "Script Task", "id":"32a91458-d432-4843-8504-bbb92e4c1a4c", "starting_time": 1095, "ending_time": 1100, delay: 0, cost: 0}
]},
{label: "Sim55", times: [
	{"label":"Service Task (Sim55)", task: "Service Task", "id":"b4f0d480-d862-405a-8f5b-b88a2898f3be", "starting_time": 1100, "ending_time": 1105, delay: 0, cost: 0},
	{"label":"User Task B (Sim55)", task: "User Task B", "id":"905f0eaf-93bd-4223-b27f-a1ac18d4862e", "starting_time": 1105, "ending_time": 1110, delay: 0, cost: 0},
	{"label":"User Task A (Sim55)", task: "User Task A", "id":"dfd0337f-2162-46bb-b47d-8e015c02eb4f", "starting_time": 1110, "ending_time": 1115, delay: 5, cost: 0},
	{"label":"Script Task (Sim55)", task: "Script Task", "id":"0a1c625d-fa69-4e35-8faf-ab99523f5b61", "starting_time": 1115, "ending_time": 1120, delay: 0, cost: 0}
]},
{label: "Sim56", times: [
	{"label":"Service Task (Sim56)", task: "Service Task", "id":"c09a2a72-7b46-49f2-b9d4-03fced6fceee", "starting_time": 1120, "ending_time": 1125, delay: 0, cost: 0},
	{"label":"User Task B (Sim56)", task: "User Task B", "id":"b5682387-dc9a-4a9a-b68f-b0a18a1e0020", "starting_time": 1125, "ending_time": 1130, delay: 0, cost: 0},
	{"label":"User Task A (Sim56)", task: "User Task A", "id":"f81a3ab6-7c5a-4b6a-be20-7650af6d3167", "starting_time": 1130, "ending_time": 1135, delay: 5, cost: 0},
	{"label":"Script Task (Sim56)", task: "Script Task", "id":"d8cc6ba3-69b6-44a8-9f43-d323110f8a73", "starting_time": 1135, "ending_time": 1140, delay: 0, cost: 0}
]},
{label: "Sim57", times: [
	{"label":"Service Task (Sim57)", task: "Service Task", "id":"d4bbe84d-424b-4e6a-88f3-1686411d218d", "starting_time": 1140, "ending_time": 1145, delay: 0, cost: 0},
	{"label":"User Task B (Sim57)", task: "User Task B", "id":"93199453-37f8-4818-86f5-eb8ad49ca5f7", "starting_time": 1145, "ending_time": 1150, delay: 0, cost: 0},
	{"label":"User Task A (Sim57)", task: "User Task A", "id":"db0b14f8-e143-408a-9865-4a6dea373ba7", "starting_time": 1150, "ending_time": 1155, delay: 5, cost: 0},
	{"label":"Script Task (Sim57)", task: "Script Task", "id":"1564adf5-86dd-47f7-b2b3-a6e56b0c1bf9", "starting_time": 1155, "ending_time": 1160, delay: 0, cost: 0}
]},
{label: "Sim58", times: [
	{"label":"Service Task (Sim58)", task: "Service Task", "id":"62f21f46-5eaf-4434-8777-8e878ec32cd1", "starting_time": 1160, "ending_time": 1165, delay: 0, cost: 0},
	{"label":"User Task A (Sim58)", task: "User Task A", "id":"ea57bbbd-89f7-49fb-a062-59f59f8b2318", "starting_time": 1165, "ending_time": 1170, delay: 0, cost: 0},
	{"label":"User Task B (Sim58)", task: "User Task B", "id":"0c44f301-0c4b-494e-a998-3d5862087fba", "starting_time": 1170, "ending_time": 1175, delay: 5, cost: 0},
	{"label":"Script Task (Sim58)", task: "Script Task", "id":"92e6e881-238c-4494-b23b-a26f78d81192", "starting_time": 1175, "ending_time": 1180, delay: 0, cost: 0}
]},
{label: "Sim59", times: [
	{"label":"Service Task (Sim59)", task: "Service Task", "id":"936a80c4-a9db-4e1f-b349-e7feef2de2db", "starting_time": 1180, "ending_time": 1185, delay: 0, cost: 0},
	{"label":"User Task A (Sim59)", task: "User Task A", "id":"d1bff30a-b8bf-4ce6-954b-981c9e226e25", "starting_time": 1185, "ending_time": 1190, delay: 0, cost: 0},
	{"label":"User Task B (Sim59)", task: "User Task B", "id":"62ecb169-f542-4b4a-940e-f201c06b8e50", "starting_time": 1190, "ending_time": 1195, delay: 5, cost: 0},
	{"label":"Script Task (Sim59)", task: "Script Task", "id":"1d442d93-1770-4f42-8d58-d1ff26a3e786", "starting_time": 1195, "ending_time": 1200, delay: 0, cost: 0}
]},
{label: "Sim6", times: [
	{"label":"Service Task (Sim6)", task: "Service Task", "id":"201fcbe3-bbb3-437b-8323-b4055c71f97d", "starting_time": 120, "ending_time": 125, delay: 0, cost: 0},
	{"label":"User Task B (Sim6)", task: "User Task B", "id":"d0846c1f-0618-4fda-b527-290891ac9215", "starting_time": 125, "ending_time": 130, delay: 0, cost: 0},
	{"label":"User Task A (Sim6)", task: "User Task A", "id":"f8833937-deac-4390-b870-f29778c537a1", "starting_time": 130, "ending_time": 135, delay: 5, cost: 0},
	{"label":"Script Task (Sim6)", task: "Script Task", "id":"fdb526bb-a7e3-4ab4-9f04-458678d38cf1", "starting_time": 135, "ending_time": 140, delay: 0, cost: 0}
]},
{label: "Sim60", times: [
	{"label":"Service Task (Sim60)", task: "Service Task", "id":"eca61c17-c64d-484f-a004-a1d32443ba3f", "starting_time": 1200, "ending_time": 1205, delay: 0, cost: 0},
	{"label":"User Task B (Sim60)", task: "User Task B", "id":"c65ec326-776f-4555-9916-02e3a43f918d", "starting_time": 1205, "ending_time": 1210, delay: 0, cost: 0},
	{"label":"User Task A (Sim60)", task: "User Task A", "id":"5a56ff7a-ab6b-483b-8c43-d0f351c44e7c", "starting_time": 1210, "ending_time": 1215, delay: 5, cost: 0},
	{"label":"Script Task (Sim60)", task: "Script Task", "id":"fe059873-25de-45e7-bc54-3a3836104251", "starting_time": 1215, "ending_time": 1220, delay: 0, cost: 0}
]},
{label: "Sim61", times: [
	{"label":"Service Task (Sim61)", task: "Service Task", "id":"693816df-8ca6-4c6e-b5ee-f795709f3bcd", "starting_time": 1220, "ending_time": 1225, delay: 0, cost: 0},
	{"label":"User Task B (Sim61)", task: "User Task B", "id":"a60efa50-8e16-4cfb-9437-a8d9edc0c753", "starting_time": 1225, "ending_time": 1230, delay: 0, cost: 0},
	{"label":"User Task A (Sim61)", task: "User Task A", "id":"49612e56-8b9b-48a5-a478-0b078476277a", "starting_time": 1230, "ending_time": 1235, delay: 5, cost: 0},
	{"label":"Script Task (Sim61)", task: "Script Task", "id":"7ae102c3-38b5-45f0-afcd-610ca8343bcd", "starting_time": 1235, "ending_time": 1240, delay: 0, cost: 0}
]},
{label: "Sim62", times: [
	{"label":"Service Task (Sim62)", task: "Service Task", "id":"aaff73b4-4c33-44b5-b97f-17d7fc95fd36", "starting_time": 1240, "ending_time": 1245, delay: 0, cost: 0},
	{"label":"User Task B (Sim62)", task: "User Task B", "id":"d5e268c5-61ad-43bf-8f33-ee323f6ce64c", "starting_time": 1245, "ending_time": 1250, delay: 0, cost: 0},
	{"label":"User Task A (Sim62)", task: "User Task A", "id":"2de20805-44fa-4b63-be5c-61a61d88afd0", "starting_time": 1250, "ending_time": 1255, delay: 5, cost: 0},
	{"label":"Script Task (Sim62)", task: "Script Task", "id":"2676b4a8-ad50-4ae6-a267-6db1c70cb0c1", "starting_time": 1255, "ending_time": 1260, delay: 0, cost: 0}
]},
{label: "Sim63", times: [
	{"label":"Service Task (Sim63)", task: "Service Task", "id":"2a55d6dc-c7fd-446c-a316-eef303d2dcfd", "starting_time": 1260, "ending_time": 1265, delay: 0, cost: 0},
	{"label":"User Task A (Sim63)", task: "User Task A", "id":"d327f362-020d-43ee-a07d-4d730839a920", "starting_time": 1265, "ending_time": 1270, delay: 0, cost: 0},
	{"label":"User Task B (Sim63)", task: "User Task B", "id":"675e64bd-7952-45cd-8349-f4fe45f6e016", "starting_time": 1270, "ending_time": 1275, delay: 5, cost: 0},
	{"label":"Script Task (Sim63)", task: "Script Task", "id":"6c40d7cf-df32-440c-b5d5-ccc106d1c902", "starting_time": 1275, "ending_time": 1280, delay: 0, cost: 0}
]},
{label: "Sim64", times: [
	{"label":"Service Task (Sim64)", task: "Service Task", "id":"18a44e7b-28dd-4f37-98f1-5308dbaccf7c", "starting_time": 1280, "ending_time": 1285, delay: 0, cost: 0},
	{"label":"User Task B (Sim64)", task: "User Task B", "id":"c5bea4a6-2059-4034-b4d6-1c1367cff5fd", "starting_time": 1285, "ending_time": 1290, delay: 0, cost: 0},
	{"label":"User Task A (Sim64)", task: "User Task A", "id":"3858ee54-e0ec-4c12-89cd-5fa9ebdd2f1b", "starting_time": 1290, "ending_time": 1295, delay: 5, cost: 0},
	{"label":"Script Task (Sim64)", task: "Script Task", "id":"4d564714-90d9-4928-add2-bfde28d9ed4b", "starting_time": 1295, "ending_time": 1300, delay: 0, cost: 0}
]},
{label: "Sim65", times: [
	{"label":"Service Task (Sim65)", task: "Service Task", "id":"bf0baccf-797a-4400-8dea-08c2b0c3c8cf", "starting_time": 1300, "ending_time": 1305, delay: 0, cost: 0},
	{"label":"User Task B (Sim65)", task: "User Task B", "id":"b332f3b9-80ed-4508-bab4-188ad7bb45f1", "starting_time": 1305, "ending_time": 1310, delay: 0, cost: 0},
	{"label":"User Task A (Sim65)", task: "User Task A", "id":"13ea9ffe-ee8a-4891-9955-4893dc8ee8ca", "starting_time": 1310, "ending_time": 1315, delay: 5, cost: 0},
	{"label":"Script Task (Sim65)", task: "Script Task", "id":"83b0117e-5b8c-4002-8af3-53c4b95294d4", "starting_time": 1315, "ending_time": 1320, delay: 0, cost: 0}
]},
{label: "Sim66", times: [
	{"label":"Service Task (Sim66)", task: "Service Task", "id":"9107cf71-12b7-4062-be34-4b6a009b6455", "starting_time": 1320, "ending_time": 1325, delay: 0, cost: 0},
	{"label":"User Task B (Sim66)", task: "User Task B", "id":"3016751d-c3c0-4d9d-9a38-eeb6eefd97bf", "starting_time": 1325, "ending_time": 1330, delay: 0, cost: 0},
	{"label":"User Task A (Sim66)", task: "User Task A", "id":"59a727f1-10df-4c27-bd71-5f1d20ef95a2", "starting_time": 1330, "ending_time": 1335, delay: 5, cost: 0},
	{"label":"Script Task (Sim66)", task: "Script Task", "id":"9ff74055-0314-4a9e-8ca3-0ea987b1e2b6", "starting_time": 1335, "ending_time": 1340, delay: 0, cost: 0}
]},
{label: "Sim67", times: [
	{"label":"Service Task (Sim67)", task: "Service Task", "id":"f3f2b71d-f904-49b5-8a00-e6cbf43803cd", "starting_time": 1340, "ending_time": 1345, delay: 0, cost: 0},
	{"label":"User Task A (Sim67)", task: "User Task A", "id":"fb7c424e-7010-41e7-9180-9aa32074a993", "starting_time": 1345, "ending_time": 1350, delay: 0, cost: 0},
	{"label":"User Task B (Sim67)", task: "User Task B", "id":"38fb24c7-c334-4708-838c-9ce9137c38f4", "starting_time": 1350, "ending_time": 1355, delay: 5, cost: 0},
	{"label":"Script Task (Sim67)", task: "Script Task", "id":"dfe04f6a-6795-4e7e-bd54-dd867962b797", "starting_time": 1355, "ending_time": 1360, delay: 0, cost: 0}
]},
{label: "Sim68", times: [
	{"label":"Service Task (Sim68)", task: "Service Task", "id":"8486c7d0-5e31-4f3a-ba23-090b1882321e", "starting_time": 1360, "ending_time": 1365, delay: 0, cost: 0},
	{"label":"User Task B (Sim68)", task: "User Task B", "id":"fffbfecc-9ae9-4468-bca4-8b587438d229", "starting_time": 1365, "ending_time": 1370, delay: 0, cost: 0},
	{"label":"User Task A (Sim68)", task: "User Task A", "id":"0818953f-37e2-4e3d-ad0b-baea70b3e635", "starting_time": 1370, "ending_time": 1375, delay: 5, cost: 0},
	{"label":"Script Task (Sim68)", task: "Script Task", "id":"6df45ca3-59a7-4280-bb75-f5d947143edc", "starting_time": 1375, "ending_time": 1380, delay: 0, cost: 0}
]},
{label: "Sim69", times: [
	{"label":"Service Task (Sim69)", task: "Service Task", "id":"07e9d34c-c2be-4f58-8e55-7feef671f5c1", "starting_time": 1380, "ending_time": 1385, delay: 0, cost: 0},
	{"label":"User Task B (Sim69)", task: "User Task B", "id":"97ccd812-92d8-4adf-a844-b1d91d1ee386", "starting_time": 1385, "ending_time": 1390, delay: 0, cost: 0},
	{"label":"User Task A (Sim69)", task: "User Task A", "id":"42db47cf-5dc8-4384-9f59-67dfbcbb9c26", "starting_time": 1390, "ending_time": 1395, delay: 5, cost: 0},
	{"label":"Script Task (Sim69)", task: "Script Task", "id":"58a23af5-16f7-4e6c-9be0-cf2b5ae73923", "starting_time": 1395, "ending_time": 1400, delay: 0, cost: 0}
]},
{label: "Sim7", times: [
	{"label":"Service Task (Sim7)", task: "Service Task", "id":"0b0b80ed-e2e3-4b5f-9aa1-ea4dbedee664", "starting_time": 140, "ending_time": 145, delay: 0, cost: 0},
	{"label":"User Task A (Sim7)", task: "User Task A", "id":"382fb91e-ae5c-47ab-a4fe-8c54d3a82f5d", "starting_time": 145, "ending_time": 150, delay: 0, cost: 0},
	{"label":"User Task B (Sim7)", task: "User Task B", "id":"642048f7-4ce4-4e9d-a502-a503085b1aa2", "starting_time": 150, "ending_time": 155, delay: 5, cost: 0},
	{"label":"Script Task (Sim7)", task: "Script Task", "id":"6362da26-64c1-4135-9923-a51a9ef551e3", "starting_time": 155, "ending_time": 160, delay: 0, cost: 0}
]},
{label: "Sim70", times: [
	{"label":"Service Task (Sim70)", task: "Service Task", "id":"12144f10-106b-4d4c-8fd1-5060e5a6e0da", "starting_time": 1400, "ending_time": 1405, delay: 0, cost: 0},
	{"label":"User Task B (Sim70)", task: "User Task B", "id":"a5a94ab0-e1f3-4a9f-8ee4-0f5f1ab414e4", "starting_time": 1405, "ending_time": 1410, delay: 0, cost: 0},
	{"label":"User Task A (Sim70)", task: "User Task A", "id":"2a0ad420-2b1d-4f38-a348-1fabf3ba9beb", "starting_time": 1410, "ending_time": 1415, delay: 5, cost: 0},
	{"label":"Script Task (Sim70)", task: "Script Task", "id":"053a4adc-9c3c-4954-9967-fe3633a890bc", "starting_time": 1415, "ending_time": 1420, delay: 0, cost: 0}
]},
{label: "Sim71", times: [
	{"label":"Service Task (Sim71)", task: "Service Task", "id":"3d8e0cab-4a03-4c1e-b8b8-62d37b72db36", "starting_time": 1420, "ending_time": 1425, delay: 0, cost: 0},
	{"label":"User Task B (Sim71)", task: "User Task B", "id":"964f4f3f-7fc5-4cb0-a432-031adfc0e3a6", "starting_time": 1425, "ending_time": 1430, delay: 0, cost: 0},
	{"label":"User Task A (Sim71)", task: "User Task A", "id":"b57891f2-7f28-4470-9cfc-a2c505d3aaef", "starting_time": 1430, "ending_time": 1435, delay: 5, cost: 0},
	{"label":"Script Task (Sim71)", task: "Script Task", "id":"444b2bf3-1c27-499d-8bb1-f7106d315431", "starting_time": 1435, "ending_time": 1440, delay: 0, cost: 0}
]},
{label: "Sim72", times: [
	{"label":"Service Task (Sim72)", task: "Service Task", "id":"99fd7654-40c8-42a7-bf03-673a0274cce2", "starting_time": 1440, "ending_time": 1445, delay: 0, cost: 0},
	{"label":"User Task B (Sim72)", task: "User Task B", "id":"1af506ec-03ab-436c-90eb-85317862e5ae", "starting_time": 1445, "ending_time": 1450, delay: 0, cost: 0},
	{"label":"User Task A (Sim72)", task: "User Task A", "id":"6283a56e-6c06-4106-ae13-ad29aceef261", "starting_time": 1450, "ending_time": 1455, delay: 5, cost: 0},
	{"label":"Script Task (Sim72)", task: "Script Task", "id":"fe25a427-79e9-4b27-b3e0-f86daf73e3d1", "starting_time": 1455, "ending_time": 1460, delay: 0, cost: 0}
]},
{label: "Sim73", times: [
	{"label":"Service Task (Sim73)", task: "Service Task", "id":"ab94ab5f-3670-4ced-89bf-312190bc8852", "starting_time": 1460, "ending_time": 1465, delay: 0, cost: 0},
	{"label":"User Task A (Sim73)", task: "User Task A", "id":"c04fa061-c52d-403e-8582-ea2dde6ab906", "starting_time": 1465, "ending_time": 1470, delay: 0, cost: 0},
	{"label":"User Task B (Sim73)", task: "User Task B", "id":"f83d3f0f-3cdc-4004-a749-6811d6a90b0f", "starting_time": 1470, "ending_time": 1475, delay: 5, cost: 0},
	{"label":"Script Task (Sim73)", task: "Script Task", "id":"e845708a-6e53-41e5-b9d6-10957c6269b2", "starting_time": 1475, "ending_time": 1480, delay: 0, cost: 0}
]},
{label: "Sim74", times: [
	{"label":"Service Task (Sim74)", task: "Service Task", "id":"5d5561fe-7fde-4516-a311-3b1db17501b4", "starting_time": 1480, "ending_time": 1485, delay: 0, cost: 0},
	{"label":"User Task A (Sim74)", task: "User Task A", "id":"cebaa8b5-3f0d-4900-a994-17f5d276fcc2", "starting_time": 1485, "ending_time": 1490, delay: 0, cost: 0},
	{"label":"User Task B (Sim74)", task: "User Task B", "id":"33f01686-22be-4bcd-931a-3612c8336873", "starting_time": 1490, "ending_time": 1495, delay: 5, cost: 0},
	{"label":"Script Task (Sim74)", task: "Script Task", "id":"413f3e06-17aa-48a2-a051-e312ef5ea8ff", "starting_time": 1495, "ending_time": 1500, delay: 0, cost: 0}
]},
{label: "Sim75", times: [
	{"label":"Service Task (Sim75)", task: "Service Task", "id":"48f3b322-5161-4087-a4ff-281a7f0cbb6c", "starting_time": 1500, "ending_time": 1505, delay: 0, cost: 0},
	{"label":"User Task A (Sim75)", task: "User Task A", "id":"b7fccfc3-aafb-400f-bf0a-5c3c3481aa71", "starting_time": 1505, "ending_time": 1510, delay: 0, cost: 0},
	{"label":"User Task B (Sim75)", task: "User Task B", "id":"f62d9768-037e-4fc1-a68d-bce94e9d3de6", "starting_time": 1510, "ending_time": 1515, delay: 5, cost: 0},
	{"label":"Script Task (Sim75)", task: "Script Task", "id":"673ebd85-3676-44c4-a9a8-e3baf8808afc", "starting_time": 1515, "ending_time": 1520, delay: 0, cost: 0}
]},
{label: "Sim76", times: [
	{"label":"Service Task (Sim76)", task: "Service Task", "id":"6d13a560-7df3-4e03-ac48-f0d9bdd6d6a7", "starting_time": 1520, "ending_time": 1525, delay: 0, cost: 0},
	{"label":"User Task B (Sim76)", task: "User Task B", "id":"f46c9f3a-8284-4239-87a1-ad24eab8d66f", "starting_time": 1525, "ending_time": 1530, delay: 0, cost: 0},
	{"label":"User Task A (Sim76)", task: "User Task A", "id":"0f3bcb11-1534-4858-b405-13ff21105d9f", "starting_time": 1530, "ending_time": 1535, delay: 5, cost: 0},
	{"label":"Script Task (Sim76)", task: "Script Task", "id":"ceeecf19-48b0-44c0-927c-6736d818dcd8", "starting_time": 1535, "ending_time": 1540, delay: 0, cost: 0}
]},
{label: "Sim77", times: [
	{"label":"Service Task (Sim77)", task: "Service Task", "id":"364f51a8-4106-4199-8fad-f9bb57633ba3", "starting_time": 1540, "ending_time": 1545, delay: 0, cost: 0},
	{"label":"User Task A (Sim77)", task: "User Task A", "id":"c1bd4d09-67ab-42e1-bd5d-148d8c0b743c", "starting_time": 1545, "ending_time": 1550, delay: 0, cost: 0},
	{"label":"User Task B (Sim77)", task: "User Task B", "id":"e2a682df-f5ed-4a13-932d-c15750e7b306", "starting_time": 1550, "ending_time": 1555, delay: 5, cost: 0},
	{"label":"Script Task (Sim77)", task: "Script Task", "id":"430043a5-d93a-4f26-9ef1-4a2c17723c80", "starting_time": 1555, "ending_time": 1560, delay: 0, cost: 0}
]},
{label: "Sim78", times: [
	{"label":"Service Task (Sim78)", task: "Service Task", "id":"1ff0b4a2-51d6-48fe-92d6-d32af3993c74", "starting_time": 1560, "ending_time": 1565, delay: 0, cost: 0},
	{"label":"User Task A (Sim78)", task: "User Task A", "id":"df11cedf-ee2c-4aa3-ae16-26898ef1b867", "starting_time": 1565, "ending_time": 1570, delay: 0, cost: 0},
	{"label":"User Task B (Sim78)", task: "User Task B", "id":"73e1081c-cb6b-4d3c-98f8-017055a3433b", "starting_time": 1570, "ending_time": 1575, delay: 5, cost: 0},
	{"label":"Script Task (Sim78)", task: "Script Task", "id":"89aa9a32-d830-4ff8-a3b4-7ae6f98330c6", "starting_time": 1575, "ending_time": 1580, delay: 0, cost: 0}
]},
{label: "Sim79", times: [
	{"label":"Service Task (Sim79)", task: "Service Task", "id":"f0dcdedd-2a50-4f96-aec8-d953a10f2183", "starting_time": 1580, "ending_time": 1585, delay: 0, cost: 0},
	{"label":"User Task A (Sim79)", task: "User Task A", "id":"2d76e9a9-7d80-445d-b95f-89edfd3dcded", "starting_time": 1585, "ending_time": 1590, delay: 0, cost: 0},
	{"label":"User Task B (Sim79)", task: "User Task B", "id":"6d7fbb9d-cc26-49fa-8ed1-0c2fbb4e69af", "starting_time": 1590, "ending_time": 1595, delay: 5, cost: 0},
	{"label":"Script Task (Sim79)", task: "Script Task", "id":"358cf623-6e73-49a2-a1d3-442d81716ade", "starting_time": 1595, "ending_time": 1600, delay: 0, cost: 0}
]},
{label: "Sim8", times: [
	{"label":"Service Task (Sim8)", task: "Service Task", "id":"ba8a6053-ea27-49d9-80eb-4f96eb8d6aac", "starting_time": 160, "ending_time": 165, delay: 0, cost: 0},
	{"label":"User Task A (Sim8)", task: "User Task A", "id":"eca94382-c539-4091-bee6-5c3c752e2731", "starting_time": 165, "ending_time": 170, delay: 0, cost: 0},
	{"label":"User Task B (Sim8)", task: "User Task B", "id":"0cb6d8e5-0f81-46cf-bb89-9ca3a18fe5dd", "starting_time": 170, "ending_time": 175, delay: 5, cost: 0},
	{"label":"Script Task (Sim8)", task: "Script Task", "id":"086f487f-fdcb-4873-a48b-63f18b072c3d", "starting_time": 175, "ending_time": 180, delay: 0, cost: 0}
]},
{label: "Sim80", times: [
	{"label":"Service Task (Sim80)", task: "Service Task", "id":"8817e7ee-9203-470e-a802-04f8104ea5df", "starting_time": 1600, "ending_time": 1605, delay: 0, cost: 0},
	{"label":"User Task A (Sim80)", task: "User Task A", "id":"82f8cfe9-fc38-4be5-af3d-20a2bbfc79f4", "starting_time": 1605, "ending_time": 1610, delay: 0, cost: 0},
	{"label":"User Task B (Sim80)", task: "User Task B", "id":"182f6280-1db2-4a0c-bbd5-8be8b6289ab5", "starting_time": 1610, "ending_time": 1615, delay: 5, cost: 0},
	{"label":"Script Task (Sim80)", task: "Script Task", "id":"06f73372-e5ef-488e-b3da-946d7a5254e3", "starting_time": 1615, "ending_time": 1620, delay: 0, cost: 0}
]},
{label: "Sim81", times: [
	{"label":"Service Task (Sim81)", task: "Service Task", "id":"d10c1efd-6e1c-4123-b430-2452a4a831e0", "starting_time": 1620, "ending_time": 1625, delay: 0, cost: 0},
	{"label":"User Task A (Sim81)", task: "User Task A", "id":"8a408212-98c6-496f-bb0a-575959bac051", "starting_time": 1625, "ending_time": 1630, delay: 0, cost: 0},
	{"label":"User Task B (Sim81)", task: "User Task B", "id":"0de47be9-c39c-4ee7-8456-755ce83de731", "starting_time": 1630, "ending_time": 1635, delay: 5, cost: 0},
	{"label":"Script Task (Sim81)", task: "Script Task", "id":"a6abe662-3954-4fd9-91f4-d094d518fa03", "starting_time": 1635, "ending_time": 1640, delay: 0, cost: 0}
]},
{label: "Sim82", times: [
	{"label":"Service Task (Sim82)", task: "Service Task", "id":"26a65a4a-d95b-471c-a3bc-d931a62aace5", "starting_time": 1640, "ending_time": 1645, delay: 0, cost: 0},
	{"label":"User Task A (Sim82)", task: "User Task A", "id":"9b9eaa43-7675-4d38-8542-603312ae5f3c", "starting_time": 1645, "ending_time": 1650, delay: 0, cost: 0},
	{"label":"User Task B (Sim82)", task: "User Task B", "id":"50014dfd-a7ae-498f-a4ba-f1de70bc5858", "starting_time": 1650, "ending_time": 1655, delay: 5, cost: 0},
	{"label":"Script Task (Sim82)", task: "Script Task", "id":"3ac3da51-3e21-47d4-9739-6ffe075e927f", "starting_time": 1655, "ending_time": 1660, delay: 0, cost: 0}
]},
{label: "Sim83", times: [
	{"label":"Service Task (Sim83)", task: "Service Task", "id":"e50912d3-4c81-4cac-862e-4305efa53021", "starting_time": 1660, "ending_time": 1665, delay: 0, cost: 0},
	{"label":"User Task B (Sim83)", task: "User Task B", "id":"8e1978e7-678e-4910-9d78-8a91a2ec0b8a", "starting_time": 1665, "ending_time": 1670, delay: 0, cost: 0},
	{"label":"User Task A (Sim83)", task: "User Task A", "id":"99cb61ab-0bc0-4296-912e-20791d70a6f9", "starting_time": 1670, "ending_time": 1675, delay: 5, cost: 0},
	{"label":"Script Task (Sim83)", task: "Script Task", "id":"b5ee6643-d363-48bb-bfed-b2bf8d9cd02a", "starting_time": 1675, "ending_time": 1680, delay: 0, cost: 0}
]},
{label: "Sim84", times: [
	{"label":"Service Task (Sim84)", task: "Service Task", "id":"e3cb0215-60ce-4585-bdd1-f508d88e4291", "starting_time": 1680, "ending_time": 1685, delay: 0, cost: 0},
	{"label":"User Task A (Sim84)", task: "User Task A", "id":"a8a611a9-75ca-4a0c-9286-e867d7b2129d", "starting_time": 1685, "ending_time": 1690, delay: 0, cost: 0},
	{"label":"User Task B (Sim84)", task: "User Task B", "id":"c95d920c-6518-4342-90a1-c0211c5e197e", "starting_time": 1690, "ending_time": 1695, delay: 5, cost: 0},
	{"label":"Script Task (Sim84)", task: "Script Task", "id":"604e1cc9-fe5c-4029-b06c-6735c72ed651", "starting_time": 1695, "ending_time": 1700, delay: 0, cost: 0}
]},
{label: "Sim85", times: [
	{"label":"Service Task (Sim85)", task: "Service Task", "id":"d5905d12-abdb-4ecc-a3a8-4557dce9fedf", "starting_time": 1700, "ending_time": 1705, delay: 0, cost: 0},
	{"label":"User Task B (Sim85)", task: "User Task B", "id":"0da86c3d-f550-49ff-aa46-0d751889a391", "starting_time": 1705, "ending_time": 1710, delay: 0, cost: 0},
	{"label":"User Task A (Sim85)", task: "User Task A", "id":"691940d2-f599-44c4-a000-eba53c8bd6d4", "starting_time": 1710, "ending_time": 1715, delay: 5, cost: 0},
	{"label":"Script Task (Sim85)", task: "Script Task", "id":"1d5f56c9-6888-44d1-ac01-b38b506217fe", "starting_time": 1715, "ending_time": 1720, delay: 0, cost: 0}
]},
{label: "Sim86", times: [
	{"label":"Service Task (Sim86)", task: "Service Task", "id":"eb6a3782-b09c-4dc2-93eb-658cf337d3e6", "starting_time": 1720, "ending_time": 1725, delay: 0, cost: 0},
	{"label":"User Task B (Sim86)", task: "User Task B", "id":"d3b0002c-e4b6-4edd-8880-f5d7c050d89a", "starting_time": 1725, "ending_time": 1730, delay: 0, cost: 0},
	{"label":"User Task A (Sim86)", task: "User Task A", "id":"44a4ba33-9c3b-4121-8c04-d0a6a0659e28", "starting_time": 1730, "ending_time": 1735, delay: 5, cost: 0},
	{"label":"Script Task (Sim86)", task: "Script Task", "id":"e98a0307-e70c-4b41-af6d-0e050890ca3f", "starting_time": 1735, "ending_time": 1740, delay: 0, cost: 0}
]},
{label: "Sim87", times: [
	{"label":"Service Task (Sim87)", task: "Service Task", "id":"9cc715e6-a193-4dc9-8ad2-0060fc292a83", "starting_time": 1740, "ending_time": 1745, delay: 0, cost: 0},
	{"label":"User Task B (Sim87)", task: "User Task B", "id":"847bdc02-caee-47ae-8d12-54f23967ff1f", "starting_time": 1745, "ending_time": 1750, delay: 0, cost: 0},
	{"label":"User Task A (Sim87)", task: "User Task A", "id":"5862239d-e27c-4d8a-918f-bda321a0bd93", "starting_time": 1750, "ending_time": 1755, delay: 5, cost: 0},
	{"label":"Script Task (Sim87)", task: "Script Task", "id":"90a40cf7-d28d-456b-b08f-0a665d0f278a", "starting_time": 1755, "ending_time": 1760, delay: 0, cost: 0}
]},
{label: "Sim88", times: [
	{"label":"Service Task (Sim88)", task: "Service Task", "id":"41627c0c-3cf5-4597-ab89-65456145e523", "starting_time": 1760, "ending_time": 1765, delay: 0, cost: 0},
	{"label":"User Task B (Sim88)", task: "User Task B", "id":"b3bc7643-6ee8-41b9-bae4-6c21af40f946", "starting_time": 1765, "ending_time": 1770, delay: 0, cost: 0},
	{"label":"User Task A (Sim88)", task: "User Task A", "id":"bd77e2ca-7e24-4582-80b2-85d180584c76", "starting_time": 1770, "ending_time": 1775, delay: 5, cost: 0},
	{"label":"Script Task (Sim88)", task: "Script Task", "id":"9d813cad-50da-4a1b-8704-deb65b214656", "starting_time": 1775, "ending_time": 1780, delay: 0, cost: 0}
]},
{label: "Sim89", times: [
	{"label":"Service Task (Sim89)", task: "Service Task", "id":"ba0eb517-9606-44c4-a253-93e30d347d2d", "starting_time": 1780, "ending_time": 1785, delay: 0, cost: 0},
	{"label":"User Task B (Sim89)", task: "User Task B", "id":"c8f8b73d-3820-47f2-961d-6af01982f0e8", "starting_time": 1785, "ending_time": 1790, delay: 0, cost: 0},
	{"label":"User Task A (Sim89)", task: "User Task A", "id":"0a734d3f-7312-4349-8efc-51139d2d5a4f", "starting_time": 1790, "ending_time": 1795, delay: 5, cost: 0},
	{"label":"Script Task (Sim89)", task: "Script Task", "id":"ad746585-92d5-409c-91c7-3b7d0f85b8ab", "starting_time": 1795, "ending_time": 1800, delay: 0, cost: 0}
]},
{label: "Sim9", times: [
	{"label":"Service Task (Sim9)", task: "Service Task", "id":"80d09e6f-4418-4d44-b160-5236090fd8c7", "starting_time": 180, "ending_time": 185, delay: 0, cost: 0},
	{"label":"User Task A (Sim9)", task: "User Task A", "id":"d9f66662-d5dc-4df8-8933-303ad6f64e30", "starting_time": 185, "ending_time": 190, delay: 0, cost: 0},
	{"label":"User Task B (Sim9)", task: "User Task B", "id":"e94ad542-80c3-48f4-aa81-87428f9e3531", "starting_time": 190, "ending_time": 195, delay: 5, cost: 0},
	{"label":"Script Task (Sim9)", task: "Script Task", "id":"d799a951-a514-4da6-8853-46b34efc3ab8", "starting_time": 195, "ending_time": 200, delay: 0, cost: 0}
]},
{label: "Sim90", times: [
	{"label":"Service Task (Sim90)", task: "Service Task", "id":"037e37bf-55f5-4b33-99ae-8b6b704bbfd7", "starting_time": 1800, "ending_time": 1805, delay: 0, cost: 0},
	{"label":"User Task A (Sim90)", task: "User Task A", "id":"ab5a5ce0-35ad-46b5-8d4b-a11f0cf10ec9", "starting_time": 1805, "ending_time": 1810, delay: 0, cost: 0},
	{"label":"User Task B (Sim90)", task: "User Task B", "id":"2e54eba3-06ed-4388-a616-9f488aabcdd0", "starting_time": 1810, "ending_time": 1815, delay: 5, cost: 0},
	{"label":"Script Task (Sim90)", task: "Script Task", "id":"2f90a78e-442f-4d19-b9a1-5b873fb13e17", "starting_time": 1815, "ending_time": 1820, delay: 0, cost: 0}
]},
{label: "Sim91", times: [
	{"label":"Service Task (Sim91)", task: "Service Task", "id":"4c26307d-b1a6-44ee-aef3-0a07378d5c3a", "starting_time": 1820, "ending_time": 1825, delay: 0, cost: 0},
	{"label":"User Task B (Sim91)", task: "User Task B", "id":"87283e43-c2bc-4691-9d10-7426f0c7028b", "starting_time": 1825, "ending_time": 1830, delay: 0, cost: 0},
	{"label":"User Task A (Sim91)", task: "User Task A", "id":"0ec4fbd8-960e-424d-b79d-09cb3acc9eee", "starting_time": 1830, "ending_time": 1835, delay: 5, cost: 0},
	{"label":"Script Task (Sim91)", task: "Script Task", "id":"47a25248-66f9-49d4-8b37-28a1282a036a", "starting_time": 1835, "ending_time": 1840, delay: 0, cost: 0}
]},
{label: "Sim92", times: [
	{"label":"Service Task (Sim92)", task: "Service Task", "id":"6833c155-96c4-4262-b8df-1835eed82753", "starting_time": 1840, "ending_time": 1845, delay: 0, cost: 0},
	{"label":"User Task B (Sim92)", task: "User Task B", "id":"83d9704e-2a6e-458a-a3b4-e96875fe067e", "starting_time": 1845, "ending_time": 1850, delay: 0, cost: 0},
	{"label":"User Task A (Sim92)", task: "User Task A", "id":"48483e94-f634-4692-8013-9c0afb8b3b6e", "starting_time": 1850, "ending_time": 1855, delay: 5, cost: 0},
	{"label":"Script Task (Sim92)", task: "Script Task", "id":"c4ca3a11-91b4-4186-9790-df0fc04b8fa5", "starting_time": 1855, "ending_time": 1860, delay: 0, cost: 0}
]},
{label: "Sim93", times: [
	{"label":"Service Task (Sim93)", task: "Service Task", "id":"fcd9d777-c2d5-4f3c-9e04-d41178a7134d", "starting_time": 1860, "ending_time": 1865, delay: 0, cost: 0},
	{"label":"User Task A (Sim93)", task: "User Task A", "id":"f6c33323-75e9-4df1-9c31-f932ba84c62f", "starting_time": 1865, "ending_time": 1870, delay: 0, cost: 0},
	{"label":"User Task B (Sim93)", task: "User Task B", "id":"068ab3e3-dcab-4184-ad8c-d54e004652d2", "starting_time": 1870, "ending_time": 1875, delay: 5, cost: 0},
	{"label":"Script Task (Sim93)", task: "Script Task", "id":"5d06525f-9d89-4225-b13a-55fa42645441", "starting_time": 1875, "ending_time": 1880, delay: 0, cost: 0}
]},
{label: "Sim94", times: [
	{"label":"Service Task (Sim94)", task: "Service Task", "id":"14a4e746-e34b-4f15-bb0a-5baa9a842419", "starting_time": 1880, "ending_time": 1885, delay: 0, cost: 0},
	{"label":"User Task B (Sim94)", task: "User Task B", "id":"60099fd0-5c14-439e-aed8-1505612ac850", "starting_time": 1885, "ending_time": 1890, delay: 0, cost: 0},
	{"label":"User Task A (Sim94)", task: "User Task A", "id":"79353634-7bd6-4cae-8fa0-9ade6b415f62", "starting_time": 1890, "ending_time": 1895, delay: 5, cost: 0},
	{"label":"Script Task (Sim94)", task: "Script Task", "id":"87e57115-ca9c-441a-a8d7-6c49f92147b3", "starting_time": 1895, "ending_time": 1900, delay: 0, cost: 0}
]},
{label: "Sim95", times: [
	{"label":"Service Task (Sim95)", task: "Service Task", "id":"1624e535-1528-4270-8326-2a464cd730da", "starting_time": 1900, "ending_time": 1905, delay: 0, cost: 0},
	{"label":"User Task A (Sim95)", task: "User Task A", "id":"d1e53231-734b-4318-a60f-a7ae9026c78a", "starting_time": 1905, "ending_time": 1910, delay: 0, cost: 0},
	{"label":"User Task B (Sim95)", task: "User Task B", "id":"4b840b80-99a8-4817-bc5e-9b33acfc4cbb", "starting_time": 1910, "ending_time": 1915, delay: 5, cost: 0},
	{"label":"Script Task (Sim95)", task: "Script Task", "id":"e7a9f046-70e3-4cb8-af58-bc6f3d986fdc", "starting_time": 1915, "ending_time": 1920, delay: 0, cost: 0}
]},
{label: "Sim96", times: [
	{"label":"Service Task (Sim96)", task: "Service Task", "id":"b2581613-30d2-4f42-ad3d-7db3ff40849e", "starting_time": 1920, "ending_time": 1925, delay: 0, cost: 0},
	{"label":"User Task B (Sim96)", task: "User Task B", "id":"06c14764-12f7-497f-aee0-061d46a13895", "starting_time": 1925, "ending_time": 1930, delay: 0, cost: 0},
	{"label":"User Task A (Sim96)", task: "User Task A", "id":"2e2c9c21-1ff5-4265-bc26-fa52a5dfbdda", "starting_time": 1930, "ending_time": 1935, delay: 5, cost: 0},
	{"label":"Script Task (Sim96)", task: "Script Task", "id":"68428239-35a1-4bc3-a137-e7e1e0ec211d", "starting_time": 1935, "ending_time": 1940, delay: 0, cost: 0}
]},
{label: "Sim97", times: [
	{"label":"Service Task (Sim97)", task: "Service Task", "id":"75bc2814-a4c2-4c46-83d9-ac48a9acbb06", "starting_time": 1940, "ending_time": 1945, delay: 0, cost: 0},
	{"label":"User Task A (Sim97)", task: "User Task A", "id":"bea53caf-b444-474a-b9d4-00feefd3cfb5", "starting_time": 1945, "ending_time": 1950, delay: 0, cost: 0},
	{"label":"User Task B (Sim97)", task: "User Task B", "id":"f593e053-640d-433f-8f1d-dcaaff4a7e9c", "starting_time": 1950, "ending_time": 1955, delay: 5, cost: 0},
	{"label":"Script Task (Sim97)", task: "Script Task", "id":"4be18272-cb4d-4f34-a429-df94e7abb89e", "starting_time": 1955, "ending_time": 1960, delay: 0, cost: 0}
]},
{label: "Sim98", times: [
	{"label":"Service Task (Sim98)", task: "Service Task", "id":"b8aaceb6-71de-4b49-9189-11d3fc69a10f", "starting_time": 1960, "ending_time": 1965, delay: 0, cost: 0},
	{"label":"User Task A (Sim98)", task: "User Task A", "id":"049738ed-9a9f-418e-aef9-9f34ca47307c", "starting_time": 1965, "ending_time": 1970, delay: 0, cost: 0},
	{"label":"User Task B (Sim98)", task: "User Task B", "id":"798002c6-4485-4b18-a0ea-dac9e6bc7efe", "starting_time": 1970, "ending_time": 1975, delay: 5, cost: 0},
	{"label":"Script Task (Sim98)", task: "Script Task", "id":"dadb542f-3882-4ca3-b8e4-49345e8a03f9", "starting_time": 1975, "ending_time": 1980, delay: 0, cost: 0}
]},
{label: "Sim99", times: [
	{"label":"Service Task (Sim99)", task: "Service Task", "id":"295fb2e0-97c0-4ad1-af91-9cf6e62103af", "starting_time": 1980, "ending_time": 1985, delay: 0, cost: 0},
	{"label":"User Task B (Sim99)", task: "User Task B", "id":"f47f4f30-cc8a-48e5-bfd5-5f07e5e87dfd", "starting_time": 1985, "ending_time": 1990, delay: 0, cost: 0},
	{"label":"User Task A (Sim99)", task: "User Task A", "id":"7b01e4ed-aec9-42b3-97ca-ee70d53b1ff1", "starting_time": 1990, "ending_time": 1995, delay: 5, cost: 0},
	{"label":"Script Task (Sim99)", task: "Script Task", "id":"9a700dcc-1eec-493c-b0a4-3b4356b8b7e8", "starting_time": 1995, "ending_time": 2000, delay: 0, cost: 0}
]},
];
