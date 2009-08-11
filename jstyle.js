/**
 *@fileOverview	jStyle, An Easy-to-Use JavaScript Based Advanced CSS  Render  Engine
 *<br/>Copyright 2009, The Creativor Studio
 *<br/>releaseDate: 2008-08-01
 *@author: <a href="mailto:creativor@163.com">Nick Zhang</a> <a href="http://creativor.spaces.live.com/">Blog</a>
 *@version: 1.00
 */
//***************Begin Sizzle CSS Selector Engine  Inlined****************/
/**
 * Sizzle CSS Selector Engine - v1.0
 *  Copyright 2009, The Dojo Foundation
 *  Released under the MIT, BSD, and GPL Licenses.
 *  More information: http://sizzlejs.com/
 *@see <a href="http://sizzlejs.com/">http://sizzlejs.com/</a>
 *@constructor
 */
if (typeof(Sizzle)=="undefined") 
 eval(function(p,a,c,k,e,r){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\b'+e(c)+'\\b','g'),k[c]);return p}('(8(){6 k=/((?:\\((?:\\([^()]+\\)|[^()]+)+\\)|\\[(?:\\[[^[\\]]*\\]|[\'"][^\'"]*[\'"]|[^[\\]\'"]+)+\\]|\\\\.|[^ >+~,(\\[\\\\]+)+|[>+~])(\\s*,\\s*)?/g,1G=0,1H=31.1I.1H,1g=F;6 n=8(a,b,c,d){c=c||[];6 e=b=b||I;5(b.C!==1&&b.C!==9){7[]}5(!a||N a!=="1s"){7 c}6 f=[],m,J,G,O,32,1J,23=z,1t=u(b);k.33=0;T((m=k.18(a))!==E){f.P(m[1]);5(m[2]){1J=24.34;X}}5(f.x>1&&p.18(a)){5(f.x===2&&o.19[f[0]]){J=v(f[0]+f[1],b)}A{J=o.19[f[0]]?[b]:n(f.1K(),b);T(f.x){a=f.1K();5(o.19[a])a+=f.1K();J=v(a,J)}}}A{5(!d&&f.x>1&&b.C===9&&!1t&&o.y.Y.L(f[0])&&!o.y.Y.L(f[f.x-1])){6 g=n.12(f.1K(),b,1t);b=g.1h?n.H(g.1h,g.J)[0]:g.J[0]}5(b){6 g=d?{1h:f.Z(),J:r(d)}:n.12(f.Z(),f.x===1&&(f[0]==="~"||f[0]==="+")&&b.13?b.13:b,1t);J=g.1h?n.H(g.1h,g.J):g.J;5(f.x>0){G=r(J)}A{23=F}T(f.x){6 h=f.Z(),Z=h;5(!o.19[h]){h=""}A{Z=f.Z()}5(Z==E){Z=b}o.19[h](G,Z,1t)}}A{G=f=[]}}5(!G){G=J}5(!G){2o"2p 2q, 2r 2s: "+(h||a);}5(1H.1L(G)==="[2t 1u]"){5(!23){c.P.1M(c,G)}A 5(b&&b.C===1){B(6 i=0;G[i]!=E;i++){5(G[i]&&(G[i]===z||G[i].C===1&&t(b,G[i]))){c.P(J[i])}}}A{B(6 i=0;G[i]!=E;i++){5(G[i]&&G[i].C===1){c.P(J[i])}}}}A{r(G,c)}5(1J){n(1J,e,c,d);n.2u(c)}7 c};n.2u=8(a){5(s){1g=F;a.35(s);5(1g){B(6 i=1;i<a.x;i++){5(a[i]===a[i-1]){a.2v(i--,1)}}}}};n.36=8(a,b){7 n(a,E,E,b)};n.12=8(a,b,c){6 d,y;5(!a){7[]}B(6 i=0,l=o.1N.x;i<l;i++){6 e=o.1N[i],y;5((y=o.y[e].18(a))){6 f=24.37;5(f.25(f.x-1)!=="\\\\"){y[1]=(y[1]||"").1a(/\\\\/g,"");d=o.12[e](y,b,c);5(d!=E){a=a.1a(o.y[e],"");X}}}}5(!d){d=b.1O("*")}7{J:d,1h:a}};n.H=8(a,b,c,d){6 e=a,U=[],14=b,y,1b,2w=b&&b[0]&&u(b[0]);T(a&&b.x){B(6 f 1P o.H){5((y=o.y[f].18(a))!=E){6 g=o.H[f],1i,1v;1b=F;5(14==U){U=[]}5(o.26[f]){y=o.26[f](y,14,c,U,d,2w);5(!y){1b=1i=z}A 5(y===z){38}}5(y){B(6 i=0;(1v=14[i])!=E;i++){5(1v){1i=g(1v,y,i,14);6 h=d^!!1i;5(c&&1i!=E){5(h){1b=z}A{14[i]=F}}A 5(h){U.P(1v);1b=z}}}}5(1i!==10){5(!c){14=U}a=a.1a(o.y[f],"");5(!1b){7[]}X}}}5(a==e){5(1b==E){2o"2p 2q, 2r 2s: "+a;}A{X}}e=a}7 14};6 o=n.39={1N:["Y","27","1j"],y:{Y:/#((?:[\\w\\1k-\\1w-]|\\\\.)+)/,1x:/\\.((?:[\\w\\1k-\\1w-]|\\\\.)+)/,27:/\\[28=[\'"]*((?:[\\w\\1k-\\1w-]|\\\\.)+)[\'"]*\\]/,29:/\\[\\s*((?:[\\w\\1k-\\1w-]|\\\\.)+)\\s*(?:(\\S?=)\\s*([\'"]*)(.*?)\\3|)\\s*\\]/,1j:/^((?:[\\w\\1k-\\3a\\*3b-]|\\\\.)+)/,1Q:/:(2x|1y|1c|1z)-3c(?:\\((1R|1S|[\\3d+-]*)\\))?/,1A:/:(1y|2y|2z|2A|1z|1c|1R|1S)(?:\\((\\d*)\\))?(?=[^-]|$)/,1B:/:((?:[\\w\\1k-\\1w-]|\\\\.)+)(?:\\(([\'"]*)((?:\\([^\\)]+\\)|[^\\2\\(\\)]*)+)\\2\\))?/},2a:{"1C":"1D","B":"3e"},1T:{1l:8(a){7 a.15("1l")}},19:{"+":8(a,b,c){6 d=N b==="1s",2b=d&&!/\\W/.L(b),2c=d&&!2b;5(2b&&!c){b=b.1m()}B(6 i=0,l=a.x,K;i<l;i++){5((K=a[i])){T((K=K.1E)&&K.C!==1){}a[i]=2c||K&&K.11===b?K||F:K===b}}5(2c){n.H(b,a,z)}},">":8(a,b,c){6 d=N b==="1s";5(d&&!/\\W/.L(b)){b=c?b:b.1m();B(6 i=0,l=a.x;i<l;i++){6 e=a[i];5(e){6 f=e.13;a[i]=f.11===b?f:F}}}A{B(6 i=0,l=a.x;i<l;i++){6 e=a[i];5(e){a[i]=d?e.13:e.13===b}}5(d){n.H(b,a,z)}}},"":8(a,b,c){6 d=1G++,1n=2d;5(!/\\W/.L(b)){6 e=b=c?b:b.1m();1n=2e}1n("13",b,d,a,e,c)},"~":8(a,b,c){6 d=1G++,1n=2d;5(N b==="1s"&&!/\\W/.L(b)){6 e=b=c?b:b.1m();1n=2e}1n("1E",b,d,a,e,c)}},12:{Y:8(a,b,c){5(N b.1F!=="10"&&!c){6 m=b.1F(a[1]);7 m?[m]:[]}},27:8(a,b,c){5(N b.2B!=="10"){6 d=[],1U=b.2B(a[1]);B(6 i=0,l=1U.x;i<l;i++){5(1U[i].15("28")===a[1]){d.P(1U[i])}}7 d.x===0?E:d}},1j:8(a,b){7 b.1O(a[1])}},26:{1x:8(a,b,c,d,e,f){a=" "+a[1].1a(/\\\\/g,"")+" ";5(f){7 a}B(6 i=0,K;(K=b[i])!=E;i++){5(K){5(e^(K.1D&&(" "+K.1D+" ").1o(a)>=0)){5(!c)d.P(K)}A 5(c){b[i]=F}}}7 F},Y:8(a){7 a[1].1a(/\\\\/g,"")},1j:8(a,b){B(6 i=0;b[i]===F;i++){}7 b[i]&&u(b[i])?a[1]:a[1].1m()},1Q:8(a){5(a[1]=="1y"){6 b=/(-?)(\\d*)n((?:\\+|-)?\\d*)/.18(a[2]=="1R"&&"2n"||a[2]=="1S"&&"2n+1"||!/\\D/.L(a[2])&&"3f+"+a[2]||a[2]);a[2]=(b[1]+(b[2]||1))-0;a[3]=b[3]-0}a[0]=1G++;7 a},29:8(a,b,c,d,e,f){6 g=a[1].1a(/\\\\/g,"");5(!f&&o.2a[g]){a[1]=o.2a[g]}5(a[2]==="~="){a[4]=" "+a[4]+" "}7 a},1B:8(a,b,c,d,e){5(a[1]==="2C"){5(k.18(a[3]).x>1||/^\\w/.L(a[3])){a[3]=n(a[3],E,E,b)}A{6 f=n.H(a[3],b,c,z^e);5(!c){d.P.1M(d,f)}7 F}}A 5(o.y.1A.L(a[0])||o.y.1Q.L(a[0])){7 z}7 a},1A:8(a){a.3g(z);7 a}},2D:{3h:8(a){7 a.2f===F&&a.V!=="3i"},2f:8(a){7 a.2f===z},2E:8(a){7 a.2E===z},2F:8(a){a.13.3j;7 a.2F===z},1p:8(a){7!!a.1d},3k:8(a){7!a.1d},3l:8(a,i,b){7!!n(b[3],a).x},3m:8(a){7/h\\d/i.L(a.11)},2G:8(a){7"2G"===a.V},2H:8(a){7"2H"===a.V},2I:8(a){7"2I"===a.V},2J:8(a){7"2J"===a.V},2K:8(a){7"2K"===a.V},2L:8(a){7"2L"===a.V},2M:8(a){7"2M"===a.V},2N:8(a){7"2N"===a.V},2g:8(a){7"2g"===a.V||a.11.1m()==="3n"},2O:8(a){7/2O|3o|3p|2g/i.L(a.11)}},2P:{1z:8(a,i){7 i===0},1c:8(a,i,b,c){7 i===c.x-1},1R:8(a,i){7 i%2===0},1S:8(a,i){7 i%2===1},2A:8(a,i,b){7 i<b[3]-0},2z:8(a,i,b){7 i>b[3]-0},1y:8(a,i,b){7 b[3]-0==i},2y:8(a,i,b){7 b[3]-0==i}},H:{1B:8(a,b,i,c){6 d=b[1],H=o.2D[d];5(H){7 H(a,i,b,c)}A 5(d==="2h"){7(a.3q||a.3r||"").1o(b[3])>=0}A 5(d==="2C"){6 e=b[3];B(i=0,l=e.x;i<l;i++){5(e[i]===a){7 F}}7 z}},1Q:8(a,b){6 c=b[1],M=a;3s(c){1V\'2x\':1V\'1z\':T((M=M.1E)){5(M.C===1)7 F}5(c==\'1z\')7 z;M=a;1V\'1c\':T((M=M.2Q)){5(M.C===1)7 F}7 z;1V\'1y\':6 d=b[2],1c=b[3];5(d==1&&1c==0){7 z}6 e=b[0],1p=a.13;5(1p&&(1p.17!==e||!a.2i)){6 f=0;B(M=1p.1d;M;M=M.2Q){5(M.C===1){M.2i=++f}}1p.17=e}6 g=a.2i-1c;5(d==0){7 g==0}A{7(g%d==0&&g/d>=0)}}},Y:8(a,b){7 a.C===1&&a.15("1e")===b},1j:8(a,b){7(b==="*"&&a.C===1)||a.11===b},1x:8(a,b){7(" "+(a.1D||a.15("1C"))+" ").1o(b)>-1},29:8(a,b){6 c=b[1],U=o.1T[c]?o.1T[c](a):a[c]!=E?a[c]:a.15(c),R=U+"",q=b[2],O=b[4];7 U==E?q==="!=":q==="="?R===O:q==="*="?R.1o(O)>=0:q==="~="?(" "+R+" ").1o(O)>=0:!O?R&&U!==F:q==="!="?R!=O:q==="^="?R.1o(O)===0:q==="$="?R.25(R.x-O.x)===O:q==="|="?R===O||R.25(0,O.x+1)===O+"-":F},1A:8(a,b,i,c){6 d=b[2],H=o.2P[d];5(H){7 H(a,i,b,c)}}}};6 p=o.y.1A;B(6 q 1P o.y){o.y[q]=2R 24(o.y[q].2S+/(?![^\\[]*\\])(?![^\\(]*\\))/.2S)}6 r=8(a,b){a=1u.1I.2T.1L(a,0);5(b){b.P.1M(b,a);7 b}7 a};2U{1u.1I.2T.1L(I.1f.3t,0)}2V(e){r=8(a,b){6 c=b||[];5(1H.1L(a)==="[2t 1u]"){1u.1I.P.1M(c,a)}A{5(N a.x==="3u"){B(6 i=0,l=a.x;i<l;i++){c.P(a[i])}}A{B(6 i=0;a[i];i++){c.P(a[i])}}}7 c}}6 s;5(I.1f.1W){s=8(a,b){6 c=a.1W(b)&4?-1:a===b?0:1;5(c===0){1g=z}7 c}}A 5("2j"1P I.1f){s=8(a,b){6 c=a.2j-b.2j;5(c===0){1g=z}7 c}}A 5(I.2k){s=8(a,b){6 c=a.1X.2k(),1Y=b.1X.2k();c.2W(a);c.2X(z);1Y.2W(b);1Y.2X(z);6 d=c.3v(3w.3x,1Y);5(d===0){1g=z}7 d}}(8(){6 d=I.1Z("Q"),1e="3y"+(2R 3z).3A();d.20="<a 28=\'"+1e+"\'/>";6 e=I.1f;e.3B(d,e.1d);5(!!I.1F(1e)){o.12.Y=8(a,b,c){5(N b.1F!=="10"&&!c){6 m=b.1F(a[1]);7 m?m.1e===a[1]||N m.21!=="10"&&m.21("1e").2Y===a[1]?[m]:10:[]}};o.H.Y=8(a,b){6 c=N a.21!=="10"&&a.21("1e");7 a.C===1&&c&&c.2Y===b}}e.3C(d);e=d=E})();(8(){6 e=I.1Z("Q");e.3D(I.3E(""));5(e.1O("*").x>0){o.12.1j=8(a,b){6 c=b.1O(a[1]);5(a[1]==="*"){6 d=[];B(6 i=0;c[i];i++){5(c[i].C===1){d.P(c[i])}}c=d}7 c}}e.20="<a 1l=\'#\'></a>";5(e.1d&&N e.1d.15!=="10"&&e.1d.15("1l")!=="#"){o.1T.1l=8(a){7 a.15("1l",2)}}e=E})();5(I.22)(8(){6 f=n,Q=I.1Z("Q");Q.20="<p 1C=\'2Z\'></p>";5(Q.22&&Q.22(".2Z").x===0){7}n=8(a,b,c,d){b=b||I;5(!d&&b.C===9&&!u(b)){2U{7 r(b.22(a),c)}2V(e){}}7 f(a,b,c,d)};B(6 g 1P f){n[g]=f[g]}Q=E})();5(I.1q&&I.1f.1q)(8(){6 d=I.1Z("Q");d.20="<Q 1C=\'L e\'></Q><Q 1C=\'L\'></Q>";5(d.1q("e").x===0)7;d.3F.1D="e";5(d.1q("e").x===1)7;o.1N.2v(1,0,"1x");o.12.1x=8(a,b,c){5(N b.1q!=="10"&&!c){7 b.1q(a[1])}};d=E})();8 2e(a,b,c,d,e,f){6 g=a=="1E"&&!f;B(6 i=0,l=d.x;i<l;i++){6 h=d[i];5(h){5(g&&h.C===1){h.17=c;h.1r=i}h=h[a];6 j=F;T(h){5(h.17===c){j=d[h.1r];X}5(h.C===1&&!f){h.17=c;h.1r=i}5(h.11===b){j=h;X}h=h[a]}d[i]=j}}}8 2d(a,b,c,d,e,f){6 g=a=="1E"&&!f;B(6 i=0,l=d.x;i<l;i++){6 h=d[i];5(h){5(g&&h.C===1){h.17=c;h.1r=i}h=h[a];6 j=F;T(h){5(h.17===c){j=d[h.1r];X}5(h.C===1){5(!f){h.17=c;h.1r=i}5(N b!=="1s"){5(h===b){j=z;X}}A 5(n.H(b,[h]).x>0){j=h;X}}h=h[a]}d[i]=j}}}6 t=I.1W?8(a,b){7 a.1W(b)&16}:8(a,b){7 a!==b&&(a.2h?a.2h(b):z)};6 u=8(a){7 a.C===9&&a.1f.11!=="30"||!!a.1X&&a.1X.1f.11!=="30"};6 v=8(a,b){6 c=[],2l="",y,2m=b.C?[b]:b;T((y=o.y.1B.18(a))){2l+=y[0];a=a.1a(o.y.1B,"")}a=o.19[a]?a+"*":a;B(6 i=0,l=2m.x;i<l;i++){n(a,2m[i],c)}7 n.H(2l,c)};3G.3H=n})();',62,230,'|||||if|var|return|function|||||||||||||||||||||||||length|match|true|else|for|nodeType||null|false|checkSet|filter|document|set|elem|test|node|typeof|check|push|div|value||while|result|type||break|ID|pop|undefined|nodeName|find|parentNode|curLoop|getAttribute||sizcache|exec|relative|replace|anyFound|last|firstChild|id|documentElement|hasDuplicate|expr|found|TAG|u00c0|href|toUpperCase|checkFn|indexOf|parent|getElementsByClassName|sizset|string|contextXML|Array|item|uFFFF_|CLASS|nth|first|POS|PSEUDO|class|className|previousSibling|getElementById|done|toString|prototype|extra|shift|call|apply|order|getElementsByTagName|in|CHILD|even|odd|attrHandle|results|case|compareDocumentPosition|ownerDocument|bRange|createElement|innerHTML|getAttributeNode|querySelectorAll|prune|RegExp|substr|preFilter|NAME|name|ATTR|attrMap|isTag|isPartStrNotTag|dirCheck|dirNodeCheck|disabled|button|contains|nodeIndex|sourceIndex|createRange|later|root||throw|Syntax|error|unrecognized|expression|object|uniqueSort|splice|isXMLFilter|only|eq|gt|lt|getElementsByName|not|filters|checked|selected|text|radio|checkbox|file|password|submit|image|reset|input|setFilters|nextSibling|new|source|slice|try|catch|selectNode|collapse|nodeValue|TEST|HTML|Object|mode|lastIndex|rightContext|sort|matches|leftContext|continue|selectors|uFFFF|_|child|dn|htmlFor|0n|unshift|enabled|hidden|selectedIndex|empty|has|header|BUTTON|select|textarea|textContent|innerText|switch|childNodes|number|compareBoundaryPoints|Range|START_TO_END|script|Date|getTime|insertBefore|removeChild|appendChild|createComment|lastChild|window|Sizzle'.split('|'),0,{}));

//***************End Sizzle CSS Selector Engine  Inline****************/
(function() {
    if (typeof HTMLElement != "undefined" && !HTMLElement.prototype.insertAdjacentElement) {
        HTMLElement.prototype.insertAdjacentElement = function(a, b) {
            switch (a) {
            case 'beforeBegin':
                this.parentNode.insertBefore(b, this);
                break;
            case 'afterBegin':
                this.insertBefore(b, this.firstChild);
                break;
            case 'beforeEnd':
                this.appendChild(b);
                break;
            case 'afterEnd':
                if (this.nextSibling) this.parentNode.insertBefore(b, this.nextSibling);
                else this.parentNode.appendChild(b);
                break
            }
        };
        HTMLElement.prototype.insertAdjacentHTML = function(a, b) {
            var r = this.ownerDocument.createRange();
            r.setStartBefore(this);
            var c = r.createContextualFragment(b);
            this.insertAdjacentElement(a, c)
        };
        HTMLElement.prototype.insertAdjacentText = function(a, b) {
            var c = document.createTextNode(b);
            this.insertAdjacentElement(a, c)
        }
    }
    var t = function() {
        var a = arguments;
        if (a.length == 1) a = [this, a[0]];
        for (var b in a[1]) {
            a[0][b] = a[1][b]
        }
        return a[0]
    };
    function isArray(a) {
        return Object.prototype.toString.apply(a) === '[object Array]'
    }
    toArray = function(a) {
        var b = a.length,
        results = new Array(b);
        while (b--) {
            results[b] = a[b]
        }
        return results
    };
    function toAbsolutePath(a, b) {
        if (/:\/\//i.test(b)) {
            return b
        }
        if (!/[\/\\]$/.test(a)) a += '/';
        a = a.substring(0, a.lastIndexOf('/'));
        while (/^\.\./.test(b)) {
            a = a.substring(0, a.lastIndexOf('/'));
            b = b.substring(3)
        }
        return a + '/' + b
    }
    var u = function() {
        var b,
        base;
        var c = "";
        b = document.getElementsByTagName('base');
        for (i = 0; i < b.length; i++) {
            if (v = b[i].href) {
                if (/^https?:\/\/[^\/]+$/.test(v)) v += '/';
                base = v ? v.match(/.*\//)[0] : ''
            }
        }
        var d = function(n) {
            var a = "";
            if (n.src) a = n.src.substring(n.src.lastIndexOf('/') + 1, n.src.length);
            if (n.src && /jstyle.*(\.dev|\.src|\.min)?.js/.test(a)) {
                c = n.src.substring(0, n.src.lastIndexOf('/'));
                if (base && c.indexOf('://') == -1) c = base + c;
                return c
            }
            return null
        };
        b = document.getElementsByTagName('script');
        for (i = 0; i < b.length; i++) {
            if (d(b[i])) break
        }
        var e = window.location.href.replace(/[\?#].*$/, '').replace(/[\/\\][^\/]+$/, '');
        c = toAbsolutePath(e, c);
        return c
    };
    var w = function(a, b) {
        if (!a || a.nodeType != 1) return false;
        var c = (b || "").split(/\s+/);
        var d;
        for (var i = 0; i < c.length; i++) {
            var e = (a.className || "").split(/\s+/);
            d = true;
            for (var j = 0; j < e.length; j++) {
                if (e[j] == c[i]) {
                    d = false;
                    break
                }
            }
            if (d) a.className += (a.className ? " ": "") + c[i]
        }
    };
    var x = function(a, b) {
        if (!a || a.nodeType != 1) return false;
        var c = (b || "").split(/\s+/);
        var d;
        for (var i = 0; i < c.length; i++) {
            var e = (a.className || "").split(/\s+/);
            d = false;
            for (var j = 0; j < e.length; j++) {
                if (e[j] == c[i]) {
                    d = true;
                    break
                }
            }
            if (d) {
                if (a.className.indexOf(c[i]) == 0) a.className = a.className.substr(c[i].length);
                else a.className = a.className.replace(" " + c[i], "")
            }
        }
    };
    var y = function(a, b) {
        if (!a || a.nodeType != 1) return false;
        var c = (b || "").split(/\s+/);
        var d;
        for (var i = 0; i < c.length; i++) {
            var e = (a.className || "").split(/\s+/);
            d = true;
            for (var j = 0; j < e.length; j++) {
                if (e[j] == c[i]) {
                    d = false;
                    break
                }
            }
            if (d) a.className += (a.className ? " ": "") + c[i];
            else {
                if (a.className.indexOf(c[i]) == 0) a.className = a.className.substr(c[i].length);
                else a.className = a.className.replace(" " + c[i], "")
            }
        }
    };
    var z = {};
    var A;
    var B;
    z = window.jStyle = window.cs$ = function(f, g, h) {
        if (f && f.nodeType === 9) {
            z.console.error("Don't suppport document as the selector");
            return null
        }
        B = f;
        var j = [];
        if (typeof(g) == "string") {
            if (" " > g || typeof(h) == "undefined") return false;
            var k = g.split(".");
            var l = [];
            var m = function(a, b) {
                if (" " > b) return;
                var c = b.split(".");
                if (!c || c.length == 0) return;
                var d = c.shift();
                if (typeof(a[d]) == "undefined") a[d] = new Object();
                var e = c.join(".");
                m(a[d], e);
                return
            };
            var n = function(a, b) {
                var c;
                if (" " > b) return a;
                c = b.split(".");
                if (c.length == 1) {
                    return a[b]
                }
                var d = c.shift();
                return n(a[d], c.join("."))
            };
            if (typeof(f) == "string") j = z.cssSelector(f);
            else if (f.length) j = f;
            else j = [f];
            var o;
            var p;
            var q = k.length;
            p = z.getStyle(k[0]);
            if (!p) return false;
            if (p.disabled) return false;
            j = z.matches(p.filter, j);
            for (var i = 0; i < j.length; i++) {
                l.length = 0;
                l = l.concat(k);
                if (" " > j[i].getAttribute("jstyle")) j[i].setAttribute("jstyle", "");
                o = z.getOrCreateElementJStyle(j[i]);
                m(o, g);
                var r = l.pop();
                var s = n(o, l.join("."));
                if (typeof(h) == "object" && !isArray(h)) {
                    t(s[r], h)
                } else {
                    s[r] = h
                }
                p.build_parameters(o[p.styleName], j[i]);
                p.render(o[p.styleName], j[i])
            }
        }
        delete j;
        return z.styles
    };
    z.loader = function() {
        z.loadLanguage(z.language);
        var a = z.cssSelector("*[" + "jstyle" + "]", document);
        var b;
        var c;
        for (var i = 0; i < z.styles.length; i++) {
            c = z.styles[i];
            if (c.disabled) continue;
            b = z.matches(c.filter, a);
            var d;
            for (var j = 0; j < b.length; j++) {
                d = z.getOrCreateElementJStyle(b[j]);
                var e;
                e = d[c.styleName];
                if (!e) {
                    continue
                }
                c.build_parameters(e, b[j]);
                if (c.render) c.render(e, b[j])
            }
            if (c.register) c.register()
        }
    };
    z.version = "1.00";
    z.cssSelector = Sizzle;
    z.matches = function(a, b) {
        return z.cssSelector.matches(a, b)
    };
    z.find = function(a, b) {
        return z.cssSelector(a, b)
    };
    z.debug = true;
    z.console = {
        error: function() {},
        log: function() {},
        info: function() {},
        assert: function() {},
        warn: function() {},
        clear: function() {}
    };
    if (z.debug && typeof(console) != "undefined") z.console = console;
    var C = u();
    z.basePath = C;
    z.t = function(a) {
        if (z.lang[a]) return z.lang[a];
        else return a
    };
    var D = function() {
        return window.ActiveXObject ? new ActiveXObject("Microsoft.XMLHTTP") : new XMLHttpRequest()
    };
    z.ajax = function(a) {
        if (" " > a.url) return null;
        var b = D();
        var c = false;
        var d = null;
        var e = null;
        var f = "GET";
        var g = function() {
            if (c) {
                return
            }
            if (b.readyState == 4) {
                if (b.status == 200) {
                    if (d) d(b.responseText, b.statusText);
                    c = true
                } else {
                    if (e) e(b, b.statusText)
                }
                b = null
            } else {}
        };
        if (typeof(a.success) == "function") d = a.success;
        if (typeof(a.error) == "function") e = a.error;
        if (" " < a.type) f = a.type;
        if (a.cache === false && f == "GET") {
            var h = +new Date();
            var i = a.url.replace(/(\?|&)_=.*?(&|$)/, "$1_=" + h + "$2");
            a.url = i + ((i == a.url) ? (a.url.match(/\?/) ? "&": "?") + "_=" + h: "")
        }
        if (z.browser.msie) b.onreadystatechange = g;
        else b.onload = g;
        b.open(f, a.url, a.async);
        b.send(null);
        return b
    };
    z.closureListener = function(b, c) {
        var d = toArray(arguments),
        __method = d.shift(),
        object = d.shift();
        return function(e) {
            e = e || window.event;
            if (e.target) {
                var a = e.target
            } else {
                var a = e.srcElement
            }
            return __method.apply(object, [e, a].concat(d))
        }
    };
    var E = navigator.userAgent.toLowerCase();
    var F = {
        version: (E.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [0, '0'])[1],
        safari: /webkit/.test(E),
        opera: /opera/.test(E),
        msie: /msie/.test(E) && !/opera/.test(E),
        mozilla: /mozilla/.test(E) && !/(compatible|webkit)/.test(E),
        userAgent: E
    };
    z.browser = F;
    var G = [];
    z.importFile = function(c, d, e) {
        c = toAbsolutePath(z.basePath, c);
        if (d == "js") {
            for (var i = 0; i < G.length; i++) {
                if (G[i] && G[i].indexOf(c) != -1) return
            }
            var f,
            script;
            f = document.getElementsByTagName("head")[0] || document.documentElement;
            if (!/^file:/i.test(c)) {
                var g = "";
                var h;
                h = z.ajax({
                    url: c,
                    success: function(a) {
                        g = a
                    },
                    async: false
                });
                if (g && /\S/.test(g)) {
                    script = document.createElement("script");
                    script.type = "text/javascript";
                    script.text = g;
                    f.insertBefore(script, f.firstChild);
                    f.removeChild(script)
                }
            } else {
                script = document.createElement("script");
                script.type = "text/javascript";
                script.src = c;
                f.insertBefore(script, f.firstChild);
                var j = false;
                var k = 0;
                var l = function(a) {
                    if (k++>10 || script.readyState == "loaded") j = true;
                    var b = new Date().getTime();
                    for (var i = 0; i < 1e7; i++) if ((new Date().getTime() - b) > a) break
                };
                while (!j) l(1)
            }
            z.console.info("imported js: " + c);
            G.push(c)
        } else if (d == "css") {
            var m = document.getElementsByTagName("link");
            for (var i = 0; i < m.length; i++) {
                if (m[i].href && m[i].href.indexOf(c) != -1) return
            }
            var s = document.createElement("link");
            s.rel = "stylesheet";
            s.type = "text/css";
            s.href = c;
            s.disabled = false;
            var f = document.getElementsByTagName("head")[0];
            f.appendChild(s)
        }
        return true
    };
    z.language = "en";
    z.lang = [];
    z.loadLanguage = function(a) {
        z.importFile("lang/" + a + ".js", "js")
    };
    var H = +new Date();
    z.getUid = function() {
        H++;
        return "jStyle" + H
    };
    z.basic_element_style = function() {
        this.srcElement = null;
        this.uid = ""
    };
    z.basic_style_parameter = function() {
        this.name = '';
        this.title = "";
        this.index = 0;
        this.builder = function(a, b) {}
    };
    z.basic_style = function() {
        this.styleName = "";
        this.disabled = false;
        this.parameterCount = 0;
        this.filter = "input,textarea,select";
        this.applyStyle = function() {
            var a = B;
            var b;
            if (arguments.length == 1) b = arguments[0];
            else {
                b = new Object();
                var c = toArray(arguments);
                var d;
                for (var i = 0; i < c.length; i++) {
                    for (var j = 0; j < this.parameters.length; j++) {
                        d = this.parameters[j];
                        if (d && d.index == i) {
                            b[d.name] = c[i];
                            break
                        }
                    }
                }
            }
            return z(a, this.styleName, b)
        };
        this.parameters = [];
        this.getParameter = function(a) {
            var b = null;
            for (var c in this.parameters) {
                if (this.parameters[c].name == a) {
                    b = this.parameters[c];
                    break
                }
            }
            return b
        };
        this.addParameter = function(a) {
            if (!a) return false;
            var b;
            b = new z.basic_style_parameter();
            if (typeof(a) == "object") {
                if (" " >= a.name) return false;
                t(b, a)
            }
            if (typeof(a) == "string") {
                b = new z.basic_style_parameter();
                b.name = a
            }
            this.parameters[b.name] = b;
            this.parameters.push(b);
            this.parameterCount++;
            if (b.index == 0) b.index = this.parameterCount - 1;
            return true
        };
        this.render = function(a, b) {};
        this.build_parameters = function(a, b) {
            var c;
            for (var d in a) {
                c = this.getParameter(d);
                if (c && c.builder) c.builder(a[d], b)
            }
        };
        this.register = function() {
            if (this.disabled) return false
        }
    };
    z.getOrCreateElementJStyle = function(a) {
        if (a["jStyle"] && typeof(a["jStyle"]) == "object") return a["jStyle"];
        var b = a.getAttribute("jstyle");
        var c = new z.basic_element_style();
        c.uid = z.getUid();
        if (" " < b) {
            b = b.replace(/(^\s*)|(\s*$)/g, "");
            if (b.substr(0, 1) != "{") {
                b = "{" + b;
                b += "}"
            }
            try {
                var d;
                d = eval('(' + b + ')')
            } catch(e) {
                z.console.error(a.id + "'s format of " + "jStyle" + ":\n\"" + b + "\"\n is wrong,\n please note that it shoulde be an JavaScript object's description!")
            }
            t(c, d)
        }
        c.srcElement = a;
        a.jStyle = c;
        return c
    };
    z.addEvent = function(a, b, c) {
        if (typeof(b) != "string") return false;
        if (a.addEventListener) a.addEventListener(b, c, false);
        else a.attachEvent("on" + b, c)
    };
    z.cancelEvent = function(a) {
        if (window.event) {
            a.returnValue = false
        } else a.preventDefault()
    };
    z.createElement = function(a, b) {
        if (" " > a) return null;
        var c = document.getElementById(a);
        if (c) return c;
        c = document.createElement(b);
        c.id = a;
        return c
    };
    z.deleteElement = function(a) {
        var b = document.getElementById(a);
        if (b) {
            b.parentNode.removeChild(b);
            return true
        }
        return false
    };
    z.styles = [];
    z.getStyle = function(a) {
        if (!a || a == "") return null;
        return z.styles[a]
    };
    z.addStyle = function(a) {
        if (!a) return false;
        if (z.getStyle(a.styleName || a)) {
            z.console.error("the style " + (a.styleName || a) + " is already exists");
            return false
        }
        var b = new z.basic_style();
        if (typeof(a) == "object") {
            if (" " >= a.styleName) return false;
            t(b, a);
            var c;
            if (b.parameters && b.parameters.length) {
                for (var i = 0; i < b.parameters.length; i++) {
                    c = b.parameters[0];
                    b.parameters.splice(0, 1);
                    if (" " < c.name) {
                        b.addParameter(c)
                    }
                }
            }
        }
        if (typeof(a) == "string") {
            b.styleName = a
        }
        var d = function() {
            return b.applyStyle.apply(b, arguments)
        };
        t(d, b);
        z.styles.push(d);
        z.styles[b.styleName] = d;
        return true
    };
    z.addStyle("css");
    z.styles.css.filter = "*";
    z.styles.css.render = function(a, b) {
        switch (typeof(a)) {
        case "object":
            for (var c in a) {
                try {
                    b.style[c] = a[c]
                } catch(e) {}
            }
            break;
        case "string":
            if (" " > a) break;
            b.style.cssText = (b.cssText || "") + a;
            break;
        default:
            break
        }
        delete b.jStyle.css
    };
    z.addStyle("classes");
    z.styles.classes.filter = "*";
    z.styles.classes.addParameter({
        name: 'op',
        index: 0
    });
    z.styles.classes.addParameter({
        name: 'content',
        index: 1
    });
    z.styles.classes.render = function(a, b) {
        if (typeof(a) != "object") return false;
        var c,
        content;
        c = a.op;
        if (typeof(c) != "string") return false;
        content = a.content;
        switch (c) {
        case "-":
        case "delete":
            x(b, content);
            break;
        case "+":
        case "add":
            w(b, content);
            break;
        case ">":
        case "toggle":
            y(b, content);
            break;
        case "=":
        default:
            b.className = content;
            break
        }
        delete b.jStyle.classes;
        return true
    };
    z.styles.cls = z.styles.classes;
    z.addStyle("manipulate");
    z.styles.manipulate.filter = "*";
    z.styles.manipulate.addParameter({
        name: 'op',
        index: 0
    });
    z.styles.manipulate.addParameter({
        name: 'content',
        index: 1
    });
    z.styles.manipulate.render = function(a, b) {
        var c,
        content;
        c = a.op || a;
        if (typeof(c) != "string") return false;
        content = a.content;
        var d = "";
        var e = [];
        var f;
        if (c != "-" && c != "delete") {
            if (content.nodeType && content.nodeType == 1) f = content.innerHTML;
            else f = content
        }
        switch (c) {
        case "-":
        case "delete":
            d = content;
            if (!d) b.parentNode.removeChild(b);
            if (typeof(d) == "string" && " " < d) {
                e = z.find(d, b);
                var g = e.length;
                for (var i = 0; i < g; i++) {
                    e[i].parentNode.removeChild(e[i])
                }
            }
            break;
        case "+<":
            b.insertAdjacentHTML("beforeBegin", f);
            break;
        case ">+":
            b.insertAdjacentHTML("afterEnd", f);
            break;
        case "+>":
            b.insertAdjacentHTML("beforeEnd", f);
            break;
        case "<+":
            b.insertAdjacentHTML("afterBegin", f);
            break;
        default:
            z.console.error("the manipulate's op: \"" + c + "\" is wrong!");
            break
        }
        delete b.jStyle.manipulate;
        return true
    };
    z.styles.mp = z.styles.manipulate;
    z.addStyle("effects");
    z.styles.effects.filter = "*";
    z.styles.effects.addParameter({
        name: 'op',
        index: 0
    });
    z.styles.effects.addParameter({
        name: 'content',
        index: 1
    });
    z.styles.effects.render = function(a, b) {
        var c,
        content;
        c = a.op || a;
        if (typeof(c) != "string") return false;
        content = a.content;
        switch (c) {
        case "-":
        case "delete":
            b.style.display = "none";
            break;
        case "+":
        case "add":
            b.style.display = "";
            break;
        case ">":
        case "toggle":
            (b.style.display == "none") ? b.style.display = "": b.style.display = "none";
            break;
        default:
            break
        }
        delete b.jStyle.effects;
        return true
    };
    z.styles.ef = z.styles.effects;
    z.addStyle("event");
    z.styles.event.filter = "*";
    z.styles.event.render = function(a, b) {
        for (var c in a) {
            if (typeof(a[c]) != "function") continue;
            z.addEvent(b, c, z.closureListener(a[c], b))
        }
        delete b.jStyle.event
    };
    z.addStyle("alert");
    z.styles.alert.filter = "*";
    z.styles.alert.render = function(a, b) {
        var c = b.jStyle.uid + "_" + "alert";
        var d = document.getElementById(c);
        if (!d && a) {
            d = z.createElement(c, "span");
            d.id = c;
            d.className = "jstyle_alert";
            b.insertAdjacentElement("afterEnd", d)
        }
        if (d && !a) z.deleteElement(c);
        if (a) d.innerHTML = a
    };
    z.addStyle("message");
    z.styles.message.filter = "*";
    z.styles.message.render = function(a, b) {
        var c = b.jStyle.uid + "_" + "message";
        var d = document.getElementById(c);
        if (!d && a) {
            d = z.createElement(c, "span");
            d.id = c;
            d.className = "jstyle_message";
            b.insertAdjacentElement("afterEnd", d)
        }
        if (d && !a) z.deleteElement(c);
        if (a) d.innerHTML = a
    };
    z.styles.msg = z.styles.message;
    z.addStyle({
        styleName: 'validation',
        filter: "form input,textarea,select",
        parameters: [{
            name: 'required',
            message: z.t("Required"),
            title: z.t("Required Item"),
            checker: function(a, b) {
                if ("" < a.value) return true
            },
            builder: function(a, b) {
                var c = b.jStyle.uid + "_validation_required";
                var d = document.getElementById(c);
                if (!d && a) {
                    var d = z.createElement(c, "span");
                    d.className = "validation_required";
                    d.id = c;
                    b.insertAdjacentElement("afterEnd", d)
                }
                if (d && a) d.title = this.title;
                if (d && !a) z.deleteElement(c)
            }
        },
        {
            name: 'min_length',
            message: z.t("The littler value is $0"),
            checker: function(a, b) {
                if (!b || " " > a.value) return true;
                if (b <= a.value.length) return true;
                return false
            }
        },
        {
            name: 'max_length',
            message: z.t("The greater value is $0"),
            checker: function(a, b) {
                if (!b || " " > a.value) return true;
                if (b >= a.value.length) return true;
                return false
            }
        },
        {
            name: 'littler',
            message: z.t("More then $0"),
            checker: function(a, b) {
                if (!b || " " > a.value) return true;
                if (b >= a.value) return true;
                return false
            }
        },
        {
            name: 'greater',
            message: z.t("Less then $0"),
            checker: function(a, b) {
                if (!b || " " > a.value) return true;
                if (b <= a.value) return true;
                return false
            }
        },
        {
            name: 'exact_length',
            message: z.t("Length is $0"),
            checker: function(a, b) {
                if (!b || " " > a.value) return true;
                if (b == a.value.length) return true;
                return false
            }
        },
        {
            name: 'alpha',
            message: z.t("Shoud be alpha"),
            checker: function(a, b) {
                if (!b || " " > a.value) return true;
                if (a.value.match(/^([a-z])+$/i)) return true;
                return false
            }
        },
        {
            name: 'alpha_numeric',
            message: z.t("Shoud be alpha or numeric"),
            checker: function(a, b) {
                if (!b || " " > a.value) return true;
                if (a.value.match(/^([a-z0-9])+$/i)) return true;
                return false
            }
        },
        {
            name: 'numeric',
            message: z.t("Shoud be numeric"),
            checker: function(a, b) {
                if (!b || " " > a.value) return true;
                if (a.value.match(/^[\-+]?[0-9]*\.?[0-9]+$/)) return true;
                return false
            }
        },
        {
            name: 'integer',
            message: z.t("Shoud be integer"),
            checker: function(a, b) {
                if (!b || " " > a.value) return true;
                if (a.value.match(/^[\-+]?[0-9]+$/i)) return true;
                return false
            }
        },
        {
            name: 'email',
            message: z.t("should be an email address"),
            checker: function(a, b) {
                if (!b || " " > a.value) return true;
                if (a.value.match(/^([a-z0-9\+_\-]+)(\.[a-z0-9\+_\-]+)*@([a-z0-9\-]+\.)+[a-z]{2,6}$/i)) return true;
                return false
            }
        },
        {
            name: 'ip',
            message: z.t("should be an ip string"),
            checker: function(a, b) {
                if (!b || " " > a.value) return true;
                var c = a.value.split(".");
                if (c.length != 4) {
                    return false
                }
                for (var i = 0; i < c.length; i++) {
                    if (c[i] == '' || !c[i].match(/[0-9]+$/) || parseInt(c[i]) > 255 || c[i].length > 3) return false
                }
                return true
            }
        },
        {
            name: 'base64',
            message: z.t("should be an base64 string"),
            checker: function(a, b) {
                if (!b || " " > a.value) return true;
                if (a.value.match(/[^a-zA-Z0-9\/\+=]/i)) return true;
                return false
            }
        },
        {
            name: 'regexp',
            message: z.t("Dose not match the specific format"),
            checker: function(a, b) {
                if (!b || " " > a.value) return true;
                if (a.value.match(eval("/" + b + "/i"))) return true;
                return false
            }
        },
        {
            name: 'matches',
            message: z.t("Dose not match"),
            checker: function(a, b) {
                if (!b) return true;
                var c,
                input_match_name,
                input_match;
                for (var i = 0; i < b.length; i++) {
                    c = b[i];
                    input_match = z.cssSelector("#" + c, a.form)[0];
                    if (!input_match) {
                        alert(z.t("Can not find the element to match ") + c);
                        return false
                    }
                    if (a.value == input_match.value) return true
                }
                return false
            }
        }],
        register: function() {
            if (this.disabled) return false;
            var c = function(e) {
                var a = true;
                var b = z.cssSelector(z.styles.validation.filter.replace("form ", ""), this);
                for (var i = 0; i < b.length; i++) {
                    if (b[i].jStyle && b[i].jStyle.validation) {
                        if (!z.styles.validation.validater(b[i])) a = false
                    }
                }
                if (!a) z.cancelEvent(e);
                return a
            };
            var d = document.getElementsByTagName("form");
            for (var i = 0; i < d.length; i++) z.addEvent(d[i], 'submit', z.closureListener(c, d[i]))
        },
        validater: function(a) {
            var b = true;
            var c = a.jStyle.validation;
            if (!c) {
                alert("Error!element " + this.id + " should has validation jstyle!");
                return false
            }
            var d;
            if (c.name) d = c.name;
            else d = a.id;
            z(a, "alert", "");
            var e = "";
            for (var i = 0; i < this.parameters.length; i++) {
                if (c[this.parameters[i].name] && this.parameters[i].checker) {
                    if (!this.parameters[i].checker(a, c[this.parameters[i].name])) {
                        e = "";
                        if (" " < this.parameters[i].message) e = this.parameters[i].message;
                        if (" " < c[this.parameters[i].name + "_message"]) e = c[this.parameters[i].name + "_message"];
                        e = e.replace("$0", c[this.parameters[i].name]);
                        if (" " < e) z(a, "alert", d + " " + e);
                        return false
                    }
                }
            }
            return b
        },
        render: function(a, b) {}
    });
    z.styles.val = z.styles.validation;
    z.addStyle({
        styleName: 'render',
        filter: "input,textarea,select",
        parameters: [{
            name: 'dialog',
            title: "",
            render_dialog: function(e) {
                var a;
                var b;
                if (this.jStyle.render && this.jStyle.render.dialog) b = this.jStyle.render.dialog;
                if (!b) {
                    alert("Error!element " + this.id + " should has render.dialog jstyle!");
                    return false
                }
                var c = new Object();
                c.text = (this.jStyle.render.dialog.text) ? this.jStyle.render.dialog.text: "";
                c.value = this.value;
                var d = "";
                b.url = b.url + ((b.url.match(/\?/)) ? "": "?") + "&value=" + c.value + "&text=" + c.text;
                if (b.features) d = b.features;
                a = window.showModalDialog(b.url, c, d);
                if (a) {
                    this.value = c.value;
                    z(this, "message", c.text)
                }
                return a
            },
            builder: function(a, b) {
                var c = b.jStyle.uid + "_" + "render_dialog";
                var d = document.getElementById(c);
                var e = "";
                if (b.jStyle.render.dialog.title) e = b.jStyle.render.dialog.title;
                var f = "";
                if (b.jStyle.render.dialog.text) f = b.jStyle.render.dialog.text;
                if (!d && a) {
                    d = z.createElement(c, "span");
                    d.id = c;
                    d.className = "render_browser";
                    d.onclick = z.closureListener(this.render_dialog, b);
                    b.insertAdjacentElement("afterEnd", d);
                    b.style.display = "none"
                }
                if (d && a) {
                    z(b, "message", f);
                    d.innerHTML = e;
                    d.title = z.t("Brower...")
                }
                if (d && !a) {
                    b.style.display = "";
                    z.deleteElement(d)
                }
            }
        },
        {
            name: 'validation',
            title: "",
            render_validation: function(e) {
                var b = false;
                if (" " > this.value) return true;
                var c;
                if (this.jStyle.render && this.jStyle.render.validation) c = this.jStyle.render.validation;
                if (!c) {
                    alert("Error!element " + this.id + " should has render.validation jstyle!");
                    return false
                }
                var d = c.url + (c.url.match(/\?/) ? "&": "?") + "value=" + this.value;
                var f = z.ajax({
                    type: "POST",
                    url: d,
                    async: false,
                    success: function(a) {
                        if (a == "1") b = true
                    }
                });
                var g = z.t("Validate");
                g += " ";
                var h = "";
                if (c.title) g = c.title;
                if (b) {
                    if (c.success_message) h = c.success_message;
                    else h = g + z.t("Success")
                } else {
                    if (c.failure_message) h = c.failure_message;
                    else h = g + z.t("Failed")
                }
                z(this, "alert", h);
                return b
            },
            builder: function(a, b) {
                var c = b.jStyle.uid + "_render_validation";
                var d = document.getElementById(c);
                var e = "";
                if (b.jStyle.render.validation.title) e = b.jStyle.render.validation.title;
                if (!d && a) {
                    d = z.createElement(c, "span");
                    d.className = "render_validation";
                    d.id = c;
                    d.onclick = z.closureListener(this.render_validation, b);
                    b.insertAdjacentElement("afterEnd", d)
                }
                if (d && a) {
                    var e = z.t("Validate");
                    if (a.title) e = a.title;
                    d.title = e;
                    d.innerHTML = e
                }
                if (d && !a) z.deleteElement(c)
            }
        },
        {
            name: 'getvalue',
            title: "",
            render_getvalue: function(e) {
                var b = "";
                var c;
                if (this.jStyle.render && this.jStyle.render.getvalue) c = this.jStyle.render.getvalue;
                if (!c) {
                    alert("Error!element " + this.id + " should has render.getvalue jstyle!");
                    return false
                }
                var d = c.url + (c.url.match(/\?/) ? "&": "?") + "value=" + this.value;
                z.ajax({
                    type: "POST",
                    url: d,
                    async: false,
                    success: function(a) {
                        if (a.length < "200") b = a
                    }
                });
                if (" " < b) this.value = b;
                return true
            },
            builder: function(a, b) {
                var c = b.jStyle.uid + "_render_getvalue";
                var d = document.getElementById(c);
                if (!d && a) {
                    d = z.createElement(c, "span");
                    d.className = "render_getvalue";
                    d.id = c;
                    d.onclick = z.closureListener(this.render_getvalue, b);
                    b.insertAdjacentElement("afterEnd", d);
                    if (a.readonly) b.readOnly = true
                }
                if (d && a) {
                    var e = z.t("Get value");
                    if (a.title) e = a.title;
                    d.title = e;
                    d.innerHTML = e
                }
                if (d && !a) {
                    z.deleteElement(c);
                    b.readOnly = false
                }
            }
        }],
        render: function(a, b) {}
    })
})();
jStyle.addEvent(window, "load", jStyle.loader);