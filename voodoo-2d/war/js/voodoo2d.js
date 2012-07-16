Voodoo2D = null;

(function(){
    "use strict";
    
    Voodoo2D = function(el)
    {
        _.bindAll();
        this.ready = false;
        this.keys = {};
        this.player = {x: 32, y: 32};
        
        this.canvas = null;
        if (!this.createCanvas(el))
        {
            throw new Exception("OH NOES!");
        }
        
        this.init();
    }
    
    Voodoo2D.prototype.bufferSize = 4;
    Voodoo2D.prototype.tileSize = 24;
    
    Voodoo2D.prototype.init = function()
    {
        var self = this;
        
        $(window).keydown(_.bind(this.keyDown, this));
        $(window).keyup(_.bind(this.keyUp, this));
        
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
    
    Voodoo2D.prototype.keyDown = function(event)
    {
        this.keys[event.which] = true;
        event.preventDefault();
    }
    
    Voodoo2D.prototype.keyUp = function(event)
    {
        this.keys[event.which] = false;
        event.preventDefault();
    }
    
    Voodoo2D.prototype.update = function()
    {
        if (this.keys[37] /* left */)
        {
            var newX = this.player.x - 1;
            if (newX >= 0 && newX < this.world.height && this.world.data[newX][this.player.y] === 0)
            {
                this.player.x = newX;            
            }
        }

        if (this.keys[38] /* up */)
        {
            var newY = this.player.y - 1;
            if (newY >= 0 && newY < this.world.height && this.world.data[this.player.x][newY] === 0)
            {
                this.player.y = newY;            
            }
        }
        
        if (this.keys[39] /* right */)
        {
            var newX = this.player.x + 1;
            if (newX >= 0 && newX < this.world.height && this.world.data[newX][this.player.y] === 0)
            {
                this.player.x = newX;            
            }
        }

        if (this.keys[40] /* up */)
        {
            var newY = this.player.y + 1;
            if (newY >= 0 && newY < this.world.height && this.world.data[this.player.x][newY] === 0)
            {
                this.player.y = newY;            
            }
        }
        
        this.render();
        _.delay(_.bind(this.update, this), 200);
    }
    
    Voodoo2D.prototype.createCanvas = function(el)
    {
        if (this.canvas) return true;

        $(el).append('<canvas id="data-canvas" style="margin-left: auto; margin-right: auto;"></canvas>');
        $(el).append('<canvas id="game-canvas" style="margin-left: auto; margin-right: auto;"></canvas>');

        this.buffer = $("#data-canvas")[0];
        this.canvas = $("#game-canvas")[0];
        
        if (!this.canvas || !this.canvas.getContext)
        {
            alert("Canvas is unsupported by your browser.");
            return false;
        }

        $("#data-canvas").click(_.bind(this.fetch, this));
        $("#game-canvas").click(_.bind(this.fetch, this));
        
        return true;
    }
    
    Voodoo2D.prototype.decompress = function(data)
    {
        this.world = {id: data.id, name: data.name, environment: data.environment, width: data.width, height: data.height, data: []};
        
        for (var x = 0; x < this.world.width; ++x)
        {
            var rawColumn = data.data[x];
            this.world.data.push(Base64Binary.decode(rawColumn));
        }        
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
        
        this.canvas.width = width * this.tileSize;
        this.canvas.height = height * this.tileSize;
        
        this.buffer.width = width * this.bufferSize;
        this.buffer.height = height * this.bufferSize;
    }   

    Voodoo2D.prototype.paintBlock = function(ctx, buf, x, y, value)
    {
        if (value != 0)
        {
            var sourceVal = value; // (value / 36.42857142857143) + 7;
            var sourceRow = sourceVal % 8;
            var sourceCol = (sourceVal - sourceRow) / 8;
            ctx.drawImage(this.tileImage, sourceCol * 80, sourceRow * 80, 80, 80, x * this.tileSize, y * this.tileSize, this.tileSize, this.tileSize);   
            
            buf.fillStyle = "rgb(" + value*13 + "," + value*13 + "," + value*13 + ")";         
        }
        else
        {
            buf.fillStyle = "rgb(0,128,255)";        
        }

        buf.fillRect(x * this.bufferSize, y * this.bufferSize, this.bufferSize, this.bufferSize);
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
                
                self.decompress(response.payload);
                self.resizeCanvas(self.world.width, self.world.height);
                self.update();
            }
        });        
    }
    
    Voodoo2D.prototype.render = function()
    {
        var canvasCtx = this.canvas.getContext("2d");
        var bufferCtx = this.buffer.getContext("2d");
        
        canvasCtx.clearRect(0, 0, this.canvas.width, this.canvas.height);
        
        for (var x = 0; x < this.world.width; ++x)
        {            
            for (var y = 0; y < this.world.height; ++y)
            {
                this.paintBlock(canvasCtx, bufferCtx, x, y, this.world.data[x][y]);
            }
        }
        
        // draw the player
        var playerX = (this.player.x * this.tileSize) - (this.tileSize / 2);
        var playerY = (this.player.y * this.tileSize) - (this.tileSize / 2);
        var fill = canvasCtx.fill;
        var stroke = canvasCtx.stroke;
        canvasCtx.fillStyle = "rgb(128, 128, 128)";
        canvasCtx.strokeStyle = "rgb(0,0,0)";
        canvasCtx.beginPath();
        canvasCtx.arc(playerX, playerY, this.tileSize / 2, 0, 7, false);
        canvasCtx.fill();
        canvasCtx.stroke();
        canvasCtx.closePath();
        canvasCtx.fillStyle = fill;
        canvasCtx.strokeStyle = stroke;
    }
})();