Voodoo2D = null;

(function(){
    "use strict";
    
    Voodoo2D = function(el)
    {
        _.bindAll();
        this.ready = false;
        
        this.canvas = null;
        if (!this.createCanvas(el))
        {
            throw new Exception("OH NOES!");
        }
        
        this.init();
    }
    
    Voodoo2D.prototype.init = function()
    {
        var self = this;
        
        $.ajax({
            url: "/voodoo2d/engine",
            success: _.bind(this.initDone, this), 
            error: _.bind(this.initFailed, this)
        });
    };
    
    Voodoo2D.prototype.initDone = function(data)
    {
        this.name = data.payload.name;
        this.version = data.payload.version;
        this.resourceRoot = data.payload.resourceRoot;
        
        $.ajax({
            url: "/voodoo2d/tileset", 
            success: _.bind(this.tilesetDone, this), 
            error: _.bind(this.tilesetFailed, this)
        });
    };
    
    Voodoo2D.prototype.initFailed = function()
    {
        alert("initFailed");
    };
    
    Voodoo2D.prototype.tilesetDone = function(data)
    {
        this.tileset = data.payload[0];
        
        if (this.tileset)
        {
            this.tileImage = new Image();
            this.tileImage.src = this.resourceRoot + "/" + this.tileset.uri;
            _.delay(_.bind(this.imageDone, this), 250);
        }
    };
    
    Voodoo2D.prototype.tilesetFailed = function()
    {
        alert("tilesetFailed");
    };
    
    Voodoo2D.prototype.imageDone = function()
    {
        if (!this.tileImage.complete)
        {
            _.delay(this.imageDone, 250);
            return;
        }
        
        this.ready = true;
    }
    
    Voodoo2D.prototype.createCanvas = function(el)
    {
        if (this.canvas) return true;
        
        $(el).append('<canvas id="game-canvas" style="margin-left: auto; margin-right: auto;"></canvas>');
        $(el).append('<canvas id="data-canvas" style="margin-left: auto; margin-right: auto;"></canvas>');
        
        this.canvas = $("#game-canvas")[0];
        this.buffer = $("#data-canvas")[0];
        
        if (!this.canvas || !this.canvas.getContext)
        {
            alert("Canvas is unsupported by your browser.");
            return false;
        }
        
        $("#game-canvas").click(_.bind(this.fetch, this));
        $("#data-canvas").click(_.bind(this.fetch, this));
        
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
        this.buffer.width = width;
        this.buffer.height = height;
    }
    
    Voodoo2D.prototype.heightColors = [
       [0, 0, 255],
       [0, 128, 255],
       [0, 255, 128],
       [64, 255, 0],
       [255, 196, 0],
       [196, 0, 0],
       [128, 96, 32],
       [128, 128, 32],
       [160, 128, 32],
       [196, 128, 32],
       [160, 160, 160],
       [255, 255, 255]
    ];
    

    Voodoo2D.prototype.paintBlock = function(ctx, buf, x, y, value)
    {
        var sourceVal = (value / 36.42857142857143) + 7;
        var sourceRow = sourceVal % 7;
        var sourceCol = (sourceVal - sourceRow) / 7;
        ctx.drawImage(this.tileImage, sourceCol * 80, sourceRow * 80, 80, 80, x * 8, y * 8, 8, 8);

        buf.fillStyle = "rgb(" + value + "," + value + "," + value + ")";
        buf.fillRect(x * 8, y * 8, 8, 8);
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
                self.resizeCanvas(self.worldData.width * 8, self.worldData.height * 8);
                self.render();
            }
        });        
    }
    
    Voodoo2D.prototype.render = function()
    {
        var canvasCtx = this.canvas.getContext("2d");
        var bufferCtx = this.buffer.getContext("2d");
        
        for (var x = 0; x < this.worldData.width; ++x)
        {
            var dataColumn = this.worldData.data[x];
            var rawColumn = Base64Binary.decode(dataColumn);
            
            for (var y = 0; y < this.worldData.height; ++y)
            {
                var dataUnit = rawColumn[y];
                this.paintBlock(canvasCtx, bufferCtx, x, y, dataUnit);
            }
        }
    }
})();