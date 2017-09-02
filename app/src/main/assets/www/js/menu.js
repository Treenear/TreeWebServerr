$(document).ready(function(){
    var $this = $(this);
                 $( this ).parent().find( 'li.active' ).removeClass( 'active' );
                 $this.addClass('active');
                  $.ajax({
                          url: '/filestorage',
                          type: "GET",
                          success: function (result) {
                              console.log(result);
                                $("#box").html(result);
                          },
                          error: function (result) { alert("Problem Occured"); },
                      });
    $('.contentone').click(function(){
          var $this = $(this);
             $( this ).parent().find( 'li.active' ).removeClass( 'active' );
             $this.addClass('active');
              $.ajax({
                      url: '/filestorage',
                      type: "GET",
                      success: function (result) {
                          console.log(result);
                            $("#box").html(result);
                      },
                      error: function (result) { alert("Problem Occured"); },
                  });
      });
    $('.contenttwo').click(function(){
          var $this = $(this);
            $( this ).parent().find( 'li.active' ).removeClass( 'active' );
            $this.addClass('active');
            $.ajax({
                     url: '/sqlitedata',
                     type: "GET",
                    success: function (result) {
                            console.log(result);

                            $("#box").html(result);
                    },
                    error: function (result) { alert("Problem Occured"); },
                    });

    });

    $('.contenttree').click(function(){
              var $this = $(this);
                $( this ).parent().find( 'li.active' ).removeClass( 'active' );
                $this.addClass('active');
                $.ajax({
                         url: '/uploadfile',
                         type: "GET",
                        success: function (result) {
                                console.log(result);
                                $("#box").html(result);
                        },
                        error: function (result) { alert("Problem Occured"); },
                        });

        });

    $('#menu').bind('click', function(){
          var $this = $(this);
              $this.closest('ul').removeClass('active');
                   $this.addClass('active');       
    });        
});