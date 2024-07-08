/**
 * 
 */

const toggleSidebar = () =>{
    
     if($(".sidebar").is(":visible")){
        // if true then off
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","1%");
     }else{
      // if false then show 
      $(".sidebar").css("display","block");
      $(".content").css("margin-left","20%");
     }
   

};