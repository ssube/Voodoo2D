if (typeof Voodoo2D === "undefined") 
{
    Voodoo2D = null;
}

(function(){
    "use strict";
    
    Voodoo2D = function(el)
    {
        _.bindAll();
        
        this.canvas = null;
        if (!this.createCanvas(el))
        {
            throw new Exception("OH NOES!");
        }
    }
    
    Voodoo2D.prototype.createCanvas = function(el)
    {
        if (this.canvas) return true;
        
        $(el).append('<canvas id="game-canvas" style="margin-left: auto; margin-right: auto;"></canvas>');
        
        this.canvas = $("#game-canvas")[0];
        
        if (!this.canvas || !this.canvas.getContext)
        {
            alert("Canvas is unsupported by your browser.");
            return false;
        }
        
        $("#game-canvas").click(_.bind(this.fetch, this));
        
        return true;
    }
    
    Voodoo2D.prototype.resizeCanvas = function(width, height)
    {
        if (!this.canvas)
        {
            console.error("Voodoo2D.resizeCanvas: canvas has not been created.");
        }
        else if (this.worldData && width && height && width != this.worldData.width && height != this.worldData.height)
        {
            console.log("Voodoo2D.resizeCanvas: resizing to a different size than the current world.");
        }
        
        this.canvas.width = width;
        this.canvas.height = height;
    }

    Voodoo2D.prototype.paintBlock = function(buffer, x, y, value)
    {
        var offset = ((y * buffer.width) + x) * 4;
        
        buffer.data[offset]      = value;
        buffer.data[offset + 1]  = value;
        buffer.data[offset + 2]  = value;
        buffer.data[offset + 3]  = 255;
        
        offset = ((y * buffer.width) + (x+1)) * 4;
        
        buffer.data[offset]      = value;
        buffer.data[offset + 1]  = value;
        buffer.data[offset + 2]  = value;
        buffer.data[offset + 3]  = 255;
        
        offset = (((y+1) * buffer.width) + x) * 4;
        
        buffer.data[offset]      = value;
        buffer.data[offset + 1]  = value;
        buffer.data[offset + 2]  = value;
        buffer.data[offset + 3]  = 255;
        
        offset = (((y+1) * buffer.width) + (x+1)) * 4;
        
        buffer.data[offset]      = value;
        buffer.data[offset + 1]  = value;
        buffer.data[offset + 2]  = value;
        buffer.data[offset + 3]  = 255;
    }
    
    Voodoo2D.prototype.fetch = function()
    {
        var self = this;
        $.ajax({
            url: "/voodoo2d/world",
            success: function(response)
            {
                if (response.error)
                {
                    console.error("Voodoo2D.fetch: returned error " + response.status + ": " + response.error);
                    return;
                }
                
                self.worldData = response.payload;
                self.resizeCanvas(self.worldData.width * 2, self.worldData.height * 2);
                self.render();
            }
        });        
    }
    
    Voodoo2D.prototype.render = function()
    {
        var canvasCtx = this.canvas.getContext("2d");
        var canvasData = canvasCtx.getImageData(0, 0, this.worldData.width * 2, this.worldData.height * 2);
        
        for (var x = 0; x < this.worldData.width; ++x)
        {
            var dataColumn = this.worldData.data[x];
            var rawColumn = Base64Binary.decode(dataColumn);
            
            for (var y = 0; y < this.worldData.height; ++y)
            {
                var dataUnit = rawColumn[y];
                this.paintBlock(canvasData, x*2, y*2, dataUnit);
            }
        }
        canvasCtx.putImageData(canvasData, 0, 0);
    }
})();