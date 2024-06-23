console.log("chirag");

const toggleSidebar =()=>{
	if($(".sidebar").is(":visible")){
		//true
		//band krna hey
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
		
	}else{
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
	}
};	