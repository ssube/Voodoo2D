if (typeof Voodoo2D === "undefined") 
{
    Voodoo2D = null;
}

(function(){
    "use strict";
    
    Voodoo2D = function(el)
    {
        this.canvas = null;
        if (!this.createCanvas(el))
        {
            throw new Exception("OH NOES!");
        }
    }
    
    Voodoo2D.prototype.createCanvas = function(el)
    {
        if (this.canvas) return true;
        
        $(el).append('<canvas id="game-canvas"></canvas>');
        
        this.canvas = $("#game-canvas")[0];
        
        if (!this.canvas || !this.canvas.getContext)
        {
            alert("Canvas is unsupported by your browser.");
            return false;
        }
        
        return true;
    }
    
    Voodoo2D.prototype.getData = function()
    {
        $.ajax({
            url: "/voodoo2d/world",
            success: function(response)
            {
                var encoded = response.data;
                var decoded = Base64Binary.decode(encoded);  
                $("#game-area").append("<ul id='game-data'>");
                for (var i = 0; i < decoded.length; ++i)
                {
                    $("#game-data").append("<li>" + decoded[i] + "</li>");                    
                }
            }
        })
    }
})();