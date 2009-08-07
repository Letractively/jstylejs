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
if (typeof(Sizzle) == "undefined") eval(function(p, a, c, k, e, r) {
    e = function(c) {
        return (c < a ? '': e(parseInt(c / a))) + ((c = c % a) > 35 ? String.fromCharCode(c + 29) : c.toString(36))
    };
    if (!''.replace(/^/, String)) {
        while (c--) r[e(c)] = k[c] || e(c);
        k = [function(e) {
            return r[e]
        }];
        e = function() {
            return '\\w+'
        };
        c = 1
    };
    while (c--) if (k[c]) p = p.replace(new RegExp('\\b' + e(c) + '\\b', 'g'), k[c]);
    return p
} ('(8(){6 k=/((?:\\((?:\\([^()]+\\)|[^()]+)+\\)|\\[(?:\\[[^[\\]]*\\]|[\'"][^\'"]*[\'"]|[^[\\]\'"]+)+\\]|\\\\.|[^ >+~,(\\[\\\\]+)+|[>+~])(\\s*,\\s*)?/g,1G=0,1H=31.1I.1H,1g=F;6 n=8(a,b,c,d){c=c||[];6 e=b=b||I;5(b.C!==1&&b.C!==9){7[]}5(!a||N a!=="1s"){7 c}6 f=[],m,J,G,O,32,1J,23=z,1t=u(b);k.33=0;T((m=k.18(a))!==E){f.P(m[1]);5(m[2]){1J=24.34;X}}5(f.x>1&&p.18(a)){5(f.x===2&&o.19[f[0]]){J=v(f[0]+f[1],b)}A{J=o.19[f[0]]?[b]:n(f.1K(),b);T(f.x){a=f.1K();5(o.19[a])a+=f.1K();J=v(a,J)}}}A{5(!d&&f.x>1&&b.C===9&&!1t&&o.y.Y.L(f[0])&&!o.y.Y.L(f[f.x-1])){6 g=n.12(f.1K(),b,1t);b=g.1h?n.H(g.1h,g.J)[0]:g.J[0]}5(b){6 g=d?{1h:f.Z(),J:r(d)}:n.12(f.Z(),f.x===1&&(f[0]==="~"||f[0]==="+")&&b.13?b.13:b,1t);J=g.1h?n.H(g.1h,g.J):g.J;5(f.x>0){G=r(J)}A{23=F}T(f.x){6 h=f.Z(),Z=h;5(!o.19[h]){h=""}A{Z=f.Z()}5(Z==E){Z=b}o.19[h](G,Z,1t)}}A{G=f=[]}}5(!G){G=J}5(!G){2o"2p 2q, 2r 2s: "+(h||a);}5(1H.1L(G)==="[2t 1u]"){5(!23){c.P.1M(c,G)}A 5(b&&b.C===1){B(6 i=0;G[i]!=E;i++){5(G[i]&&(G[i]===z||G[i].C===1&&t(b,G[i]))){c.P(J[i])}}}A{B(6 i=0;G[i]!=E;i++){5(G[i]&&G[i].C===1){c.P(J[i])}}}}A{r(G,c)}5(1J){n(1J,e,c,d);n.2u(c)}7 c};n.2u=8(a){5(s){1g=F;a.35(s);5(1g){B(6 i=1;i<a.x;i++){5(a[i]===a[i-1]){a.2v(i--,1)}}}}};n.36=8(a,b){7 n(a,E,E,b)};n.12=8(a,b,c){6 d,y;5(!a){7[]}B(6 i=0,l=o.1N.x;i<l;i++){6 e=o.1N[i],y;5((y=o.y[e].18(a))){6 f=24.37;5(f.25(f.x-1)!=="\\\\"){y[1]=(y[1]||"").1a(/\\\\/g,"");d=o.12[e](y,b,c);5(d!=E){a=a.1a(o.y[e],"");X}}}}5(!d){d=b.1O("*")}7{J:d,1h:a}};n.H=8(a,b,c,d){6 e=a,U=[],14=b,y,1b,2w=b&&b[0]&&u(b[0]);T(a&&b.x){B(6 f 1P o.H){5((y=o.y[f].18(a))!=E){6 g=o.H[f],1i,1v;1b=F;5(14==U){U=[]}5(o.26[f]){y=o.26[f](y,14,c,U,d,2w);5(!y){1b=1i=z}A 5(y===z){38}}5(y){B(6 i=0;(1v=14[i])!=E;i++){5(1v){1i=g(1v,y,i,14);6 h=d^!!1i;5(c&&1i!=E){5(h){1b=z}A{14[i]=F}}A 5(h){U.P(1v);1b=z}}}}5(1i!==10){5(!c){14=U}a=a.1a(o.y[f],"");5(!1b){7[]}X}}}5(a==e){5(1b==E){2o"2p 2q, 2r 2s: "+a;}A{X}}e=a}7 14};6 o=n.39={1N:["Y","27","1j"],y:{Y:/#((?:[\\w\\1k-\\1w-]|\\\\.)+)/,1x:/\\.((?:[\\w\\1k-\\1w-]|\\\\.)+)/,27:/\\[28=[\'"]*((?:[\\w\\1k-\\1w-]|\\\\.)+)[\'"]*\\]/,29:/\\[\\s*((?:[\\w\\1k-\\1w-]|\\\\.)+)\\s*(?:(\\S?=)\\s*([\'"]*)(.*?)\\3|)\\s*\\]/,1j:/^((?:[\\w\\1k-\\3a\\*3b-]|\\\\.)+)/,1Q:/:(2x|1y|1c|1z)-3c(?:\\((1R|1S|[\\3d+-]*)\\))?/,1A:/:(1y|2y|2z|2A|1z|1c|1R|1S)(?:\\((\\d*)\\))?(?=[^-]|$)/,1B:/:((?:[\\w\\1k-\\1w-]|\\\\.)+)(?:\\(([\'"]*)((?:\\([^\\)]+\\)|[^\\2\\(\\)]*)+)\\2\\))?/},2a:{"1C":"1D","B":"3e"},1T:{1l:8(a){7 a.15("1l")}},19:{"+":8(a,b,c){6 d=N b==="1s",2b=d&&!/\\W/.L(b),2c=d&&!2b;5(2b&&!c){b=b.1m()}B(6 i=0,l=a.x,K;i<l;i++){5((K=a[i])){T((K=K.1E)&&K.C!==1){}a[i]=2c||K&&K.11===b?K||F:K===b}}5(2c){n.H(b,a,z)}},">":8(a,b,c){6 d=N b==="1s";5(d&&!/\\W/.L(b)){b=c?b:b.1m();B(6 i=0,l=a.x;i<l;i++){6 e=a[i];5(e){6 f=e.13;a[i]=f.11===b?f:F}}}A{B(6 i=0,l=a.x;i<l;i++){6 e=a[i];5(e){a[i]=d?e.13:e.13===b}}5(d){n.H(b,a,z)}}},"":8(a,b,c){6 d=1G++,1n=2d;5(!/\\W/.L(b)){6 e=b=c?b:b.1m();1n=2e}1n("13",b,d,a,e,c)},"~":8(a,b,c){6 d=1G++,1n=2d;5(N b==="1s"&&!/\\W/.L(b)){6 e=b=c?b:b.1m();1n=2e}1n("1E",b,d,a,e,c)}},12:{Y:8(a,b,c){5(N b.1F!=="10"&&!c){6 m=b.1F(a[1]);7 m?[m]:[]}},27:8(a,b,c){5(N b.2B!=="10"){6 d=[],1U=b.2B(a[1]);B(6 i=0,l=1U.x;i<l;i++){5(1U[i].15("28")===a[1]){d.P(1U[i])}}7 d.x===0?E:d}},1j:8(a,b){7 b.1O(a[1])}},26:{1x:8(a,b,c,d,e,f){a=" "+a[1].1a(/\\\\/g,"")+" ";5(f){7 a}B(6 i=0,K;(K=b[i])!=E;i++){5(K){5(e^(K.1D&&(" "+K.1D+" ").1o(a)>=0)){5(!c)d.P(K)}A 5(c){b[i]=F}}}7 F},Y:8(a){7 a[1].1a(/\\\\/g,"")},1j:8(a,b){B(6 i=0;b[i]===F;i++){}7 b[i]&&u(b[i])?a[1]:a[1].1m()},1Q:8(a){5(a[1]=="1y"){6 b=/(-?)(\\d*)n((?:\\+|-)?\\d*)/.18(a[2]=="1R"&&"2n"||a[2]=="1S"&&"2n+1"||!/\\D/.L(a[2])&&"3f+"+a[2]||a[2]);a[2]=(b[1]+(b[2]||1))-0;a[3]=b[3]-0}a[0]=1G++;7 a},29:8(a,b,c,d,e,f){6 g=a[1].1a(/\\\\/g,"");5(!f&&o.2a[g]){a[1]=o.2a[g]}5(a[2]==="~="){a[4]=" "+a[4]+" "}7 a},1B:8(a,b,c,d,e){5(a[1]==="2C"){5(k.18(a[3]).x>1||/^\\w/.L(a[3])){a[3]=n(a[3],E,E,b)}A{6 f=n.H(a[3],b,c,z^e);5(!c){d.P.1M(d,f)}7 F}}A 5(o.y.1A.L(a[0])||o.y.1Q.L(a[0])){7 z}7 a},1A:8(a){a.3g(z);7 a}},2D:{3h:8(a){7 a.2f===F&&a.V!=="3i"},2f:8(a){7 a.2f===z},2E:8(a){7 a.2E===z},2F:8(a){a.13.3j;7 a.2F===z},1p:8(a){7!!a.1d},3k:8(a){7!a.1d},3l:8(a,i,b){7!!n(b[3],a).x},3m:8(a){7/h\\d/i.L(a.11)},2G:8(a){7"2G"===a.V},2H:8(a){7"2H"===a.V},2I:8(a){7"2I"===a.V},2J:8(a){7"2J"===a.V},2K:8(a){7"2K"===a.V},2L:8(a){7"2L"===a.V},2M:8(a){7"2M"===a.V},2N:8(a){7"2N"===a.V},2g:8(a){7"2g"===a.V||a.11.1m()==="3n"},2O:8(a){7/2O|3o|3p|2g/i.L(a.11)}},2P:{1z:8(a,i){7 i===0},1c:8(a,i,b,c){7 i===c.x-1},1R:8(a,i){7 i%2===0},1S:8(a,i){7 i%2===1},2A:8(a,i,b){7 i<b[3]-0},2z:8(a,i,b){7 i>b[3]-0},1y:8(a,i,b){7 b[3]-0==i},2y:8(a,i,b){7 b[3]-0==i}},H:{1B:8(a,b,i,c){6 d=b[1],H=o.2D[d];5(H){7 H(a,i,b,c)}A 5(d==="2h"){7(a.3q||a.3r||"").1o(b[3])>=0}A 5(d==="2C"){6 e=b[3];B(i=0,l=e.x;i<l;i++){5(e[i]===a){7 F}}7 z}},1Q:8(a,b){6 c=b[1],M=a;3s(c){1V\'2x\':1V\'1z\':T((M=M.1E)){5(M.C===1)7 F}5(c==\'1z\')7 z;M=a;1V\'1c\':T((M=M.2Q)){5(M.C===1)7 F}7 z;1V\'1y\':6 d=b[2],1c=b[3];5(d==1&&1c==0){7 z}6 e=b[0],1p=a.13;5(1p&&(1p.17!==e||!a.2i)){6 f=0;B(M=1p.1d;M;M=M.2Q){5(M.C===1){M.2i=++f}}1p.17=e}6 g=a.2i-1c;5(d==0){7 g==0}A{7(g%d==0&&g/d>=0)}}},Y:8(a,b){7 a.C===1&&a.15("1e")===b},1j:8(a,b){7(b==="*"&&a.C===1)||a.11===b},1x:8(a,b){7(" "+(a.1D||a.15("1C"))+" ").1o(b)>-1},29:8(a,b){6 c=b[1],U=o.1T[c]?o.1T[c](a):a[c]!=E?a[c]:a.15(c),R=U+"",q=b[2],O=b[4];7 U==E?q==="!=":q==="="?R===O:q==="*="?R.1o(O)>=0:q==="~="?(" "+R+" ").1o(O)>=0:!O?R&&U!==F:q==="!="?R!=O:q==="^="?R.1o(O)===0:q==="$="?R.25(R.x-O.x)===O:q==="|="?R===O||R.25(0,O.x+1)===O+"-":F},1A:8(a,b,i,c){6 d=b[2],H=o.2P[d];5(H){7 H(a,i,b,c)}}}};6 p=o.y.1A;B(6 q 1P o.y){o.y[q]=2R 24(o.y[q].2S+/(?![^\\[]*\\])(?![^\\(]*\\))/.2S)}6 r=8(a,b){a=1u.1I.2T.1L(a,0);5(b){b.P.1M(b,a);7 b}7 a};2U{1u.1I.2T.1L(I.1f.3t,0)}2V(e){r=8(a,b){6 c=b||[];5(1H.1L(a)==="[2t 1u]"){1u.1I.P.1M(c,a)}A{5(N a.x==="3u"){B(6 i=0,l=a.x;i<l;i++){c.P(a[i])}}A{B(6 i=0;a[i];i++){c.P(a[i])}}}7 c}}6 s;5(I.1f.1W){s=8(a,b){6 c=a.1W(b)&4?-1:a===b?0:1;5(c===0){1g=z}7 c}}A 5("2j"1P I.1f){s=8(a,b){6 c=a.2j-b.2j;5(c===0){1g=z}7 c}}A 5(I.2k){s=8(a,b){6 c=a.1X.2k(),1Y=b.1X.2k();c.2W(a);c.2X(z);1Y.2W(b);1Y.2X(z);6 d=c.3v(3w.3x,1Y);5(d===0){1g=z}7 d}}(8(){6 d=I.1Z("Q"),1e="3y"+(2R 3z).3A();d.20="<a 28=\'"+1e+"\'/>";6 e=I.1f;e.3B(d,e.1d);5(!!I.1F(1e)){o.12.Y=8(a,b,c){5(N b.1F!=="10"&&!c){6 m=b.1F(a[1]);7 m?m.1e===a[1]||N m.21!=="10"&&m.21("1e").2Y===a[1]?[m]:10:[]}};o.H.Y=8(a,b){6 c=N a.21!=="10"&&a.21("1e");7 a.C===1&&c&&c.2Y===b}}e.3C(d);e=d=E})();(8(){6 e=I.1Z("Q");e.3D(I.3E(""));5(e.1O("*").x>0){o.12.1j=8(a,b){6 c=b.1O(a[1]);5(a[1]==="*"){6 d=[];B(6 i=0;c[i];i++){5(c[i].C===1){d.P(c[i])}}c=d}7 c}}e.20="<a 1l=\'#\'></a>";5(e.1d&&N e.1d.15!=="10"&&e.1d.15("1l")!=="#"){o.1T.1l=8(a){7 a.15("1l",2)}}e=E})();5(I.22)(8(){6 f=n,Q=I.1Z("Q");Q.20="<p 1C=\'2Z\'></p>";5(Q.22&&Q.22(".2Z").x===0){7}n=8(a,b,c,d){b=b||I;5(!d&&b.C===9&&!u(b)){2U{7 r(b.22(a),c)}2V(e){}}7 f(a,b,c,d)};B(6 g 1P f){n[g]=f[g]}Q=E})();5(I.1q&&I.1f.1q)(8(){6 d=I.1Z("Q");d.20="<Q 1C=\'L e\'></Q><Q 1C=\'L\'></Q>";5(d.1q("e").x===0)7;d.3F.1D="e";5(d.1q("e").x===1)7;o.1N.2v(1,0,"1x");o.12.1x=8(a,b,c){5(N b.1q!=="10"&&!c){7 b.1q(a[1])}};d=E})();8 2e(a,b,c,d,e,f){6 g=a=="1E"&&!f;B(6 i=0,l=d.x;i<l;i++){6 h=d[i];5(h){5(g&&h.C===1){h.17=c;h.1r=i}h=h[a];6 j=F;T(h){5(h.17===c){j=d[h.1r];X}5(h.C===1&&!f){h.17=c;h.1r=i}5(h.11===b){j=h;X}h=h[a]}d[i]=j}}}8 2d(a,b,c,d,e,f){6 g=a=="1E"&&!f;B(6 i=0,l=d.x;i<l;i++){6 h=d[i];5(h){5(g&&h.C===1){h.17=c;h.1r=i}h=h[a];6 j=F;T(h){5(h.17===c){j=d[h.1r];X}5(h.C===1){5(!f){h.17=c;h.1r=i}5(N b!=="1s"){5(h===b){j=z;X}}A 5(n.H(b,[h]).x>0){j=h;X}}h=h[a]}d[i]=j}}}6 t=I.1W?8(a,b){7 a.1W(b)&16}:8(a,b){7 a!==b&&(a.2h?a.2h(b):z)};6 u=8(a){7 a.C===9&&a.1f.11!=="30"||!!a.1X&&a.1X.1f.11!=="30"};6 v=8(a,b){6 c=[],2l="",y,2m=b.C?[b]:b;T((y=o.y.1B.18(a))){2l+=y[0];a=a.1a(o.y.1B,"")}a=o.19[a]?a+"*":a;B(6 i=0,l=2m.x;i<l;i++){n(a,2m[i],c)}7 n.H(2l,c)};3G.3H=n})();', 62, 230, '|||||if|var|return|function|||||||||||||||||||||||||length|match|true|else|for|nodeType||null|false|checkSet|filter|document|set|elem|test|node|typeof|check|push|div|value||while|result|type||break|ID|pop|undefined|nodeName|find|parentNode|curLoop|getAttribute||sizcache|exec|relative|replace|anyFound|last|firstChild|id|documentElement|hasDuplicate|expr|found|TAG|u00c0|href|toUpperCase|checkFn|indexOf|parent|getElementsByClassName|sizset|string|contextXML|Array|item|uFFFF_|CLASS|nth|first|POS|PSEUDO|class|className|previousSibling|getElementById|done|toString|prototype|extra|shift|call|apply|order|getElementsByTagName|in|CHILD|even|odd|attrHandle|results|case|compareDocumentPosition|ownerDocument|bRange|createElement|innerHTML|getAttributeNode|querySelectorAll|prune|RegExp|substr|preFilter|NAME|name|ATTR|attrMap|isTag|isPartStrNotTag|dirCheck|dirNodeCheck|disabled|button|contains|nodeIndex|sourceIndex|createRange|later|root||throw|Syntax|error|unrecognized|expression|object|uniqueSort|splice|isXMLFilter|only|eq|gt|lt|getElementsByName|not|filters|checked|selected|text|radio|checkbox|file|password|submit|image|reset|input|setFilters|nextSibling|new|source|slice|try|catch|selectNode|collapse|nodeValue|TEST|HTML|Object|mode|lastIndex|rightContext|sort|matches|leftContext|continue|selectors|uFFFF|_|child|dn|htmlFor|0n|unshift|enabled|hidden|selectedIndex|empty|has|header|BUTTON|select|textarea|textContent|innerText|switch|childNodes|number|compareBoundaryPoints|Range|START_TO_END|script|Date|getTime|insertBefore|removeChild|appendChild|createComment|lastChild|window|Sizzle'.split('|'), 0, {}));
//***************End Sizzle CSS Selector Engine  Inline****************/

(function() {
    if (window.Node && !HTMLElement.insertAdjacentElement) {
        HTMLElement.prototype.insertAdjacentElement = function(where, parsedNode) {
            switch (where) {
            case "beforeBegin":
                this.parentNode.insertBefore(parsedNode, this);
                break;
            case "afterBegin":
                this.insertBefore(parsedNode, this.firstChild);
                break;
            case "beforeEnd":
                this.appendChild(parsedNode);
                break;
            case "afterEnd":
                if (this.nextSibling) this.parentNode.insertBefore(parsedNode, this.nextSibling);
                else this.parentNode.appendChild(parsedNode);
                break;
            }
        };
    }
    var extendObject = function() {
        var args = arguments;
        if (args.length == 1) args = [this, args[0]];
        for (var prop in args[1]) {
            args[0][prop] = args[1][prop];
        }
        args[0].__supper = args[1];
        return args[0];
    };
    function toAbsolutePath(loc, url) {
        if (/:\/\//i.test(url)) {
            return url;
        }
        if (!/[\/\\]$/.test(loc)) loc += '/';
        loc = loc.substring(0, loc.lastIndexOf('/'));
        while (/^\.\./.test(url)) {
            loc = loc.substring(0, loc.lastIndexOf('/'));
            url = url.substring(3);
        }
        return loc + '/' + url;
    };
    var getJStyleBasePath = function() {
        var nl,
        base;
        var jStyle_basePath = "";
        nl = document.getElementsByTagName('base');
        for (i = 0; i < nl.length; i++) {
            if (v = nl[i].href) {
                if (/^https?:\/\/[^\/]+$/.test(v)) v += '/';
                base = v ? v.match(/.*\//)[0] : '';
            }
        }
        var getBase = function(n) {
            var fileName = "";
            if (n.src) fileName = n.src.substring(n.src.lastIndexOf('/') + 1, n.src.length);
            if (n.src && /jstyle(\.dev|\.src|\.min)?.js/.test(fileName)) {
                jStyle_basePath = n.src.substring(0, n.src.lastIndexOf('/'));
                if (base && jStyle_basePath.indexOf('://') == -1) jStyle_basePath = base + jStyle_basePath;
                return jStyle_basePath;
            }
            return null;
        };
        nl = document.getElementsByTagName('script');
        for (i = 0; i < nl.length; i++) {
            if (getBase(nl[i])) break;
        }
        var documentBaseURL = window.location.href.replace(/[\?#].*$/, '').replace(/[\/\\][^\/]+$/, '');
        jStyle_basePath = toAbsolutePath(documentBaseURL, jStyle_basePath);
        return jStyle_basePath;
    };
    var jStyle = {};
    var cs$;
    jStyle = window.jStyle = window.cs$ = function(selector, stylepath, jstyle) {
        if (!selector || " " > stylepath || typeof(jstyle) == "undefined") return false;
        var elements = [];
        var temp_style_paths = stylepath.split(".");
        var style_paths = [];
        var makeChildObject = function(parentObject, children_string) {
            if (" " > children_string) return;
            var strings = children_string.split(".");
            if (!strings || strings.length == 0) return;
            var child_string = strings.shift();
            if (typeof(parentObject[child_string]) == "undefined") parentObject[child_string] = new Object();
            var left_children_string = strings.join(".");
            makeChildObject(parentObject[child_string], left_children_string);
            return;
        };
        var getChildObject = function(parentObject, children_string) {
            var strings;
            if (" " > children_string) return parentObject;
            strings = children_string.split(".");
            if (strings.length == 1) {
                return parentObject[children_string];
            }
            var child_string = strings.shift();
            return getChildObject(parentObject[child_string], strings.join("."));
        };
        if (typeof(selector) == "string") elements = jStyle.cssSelector(selector);
        else if (selector.length) elements = selector;
        else elements = [selector];
        var element_style;
        var style_setting;
        style_path_length = temp_style_paths.length;
        style_setting = jStyle.getStyle(temp_style_paths[0]);
        if (!style_setting) return false;
        if (style_setting.disabled) return false;
        elements = jStyle.cssSelector.matches(style_setting.filter, elements);
        for (var i = 0; i < elements.length; i++) {
            style_paths.length = 0;
            style_paths = style_paths.concat(temp_style_paths);
            if (" " > elements[i].getAttribute("jstyle")) elements[i].setAttribute("jstyle", "");
            element_style = jStyle.getOrCreateElementJStyle(elements[i]);
            makeChildObject(element_style, stylepath);
            var style_define_name = style_paths.pop();
            var element_style_define_object = getChildObject(element_style, style_paths.join("."));
            if (typeof(jstyle) == "object" || typeof(jstyle) == "function") {
                extendObject(element_style_define_object[style_define_name], jstyle);
            } else {
                element_style_define_object[style_define_name] = jstyle;
            }
            style_setting.build_parameters(element_style[style_setting.name], elements[i]);
            style_setting.render(element_style[style_setting.name], elements[i]);
        }
        delete elements;
        return true;
    };
    jStyle.loader = function() {
        jStyle.changeLanguage(jStyle.language);
        var jStyle_all_elements = jStyle.cssSelector("*[" + "jstyle" + "]", document);
        var elements_of_style;
        var jstylesetting;
        for (var i = 0; i < jStyle.styles.length; i++) {
            jstylesetting = jStyle.styles[i];
            if (jstylesetting.disabled) continue;
            elements_of_style = jStyle.cssSelector.matches(jstylesetting.filter, jStyle_all_elements);
            var element_jstyle;
            for (var j = 0; j < elements_of_style.length; j++) {
                element_jstyle = jStyle.getOrCreateElementJStyle(elements_of_style[j]);
                var cur_jstyle;
                cur_jstyle = element_jstyle[jstylesetting.name];
                if (!cur_jstyle) {
                    continue;
                }
                jstylesetting.build_parameters(cur_jstyle, elements_of_style[j]);
                if (jstylesetting.render) jstylesetting.render(cur_jstyle, elements_of_style[j]);
            }
            if (jstylesetting.register) jstylesetting.register();
        }
    };
    jStyle.version = "1.00";
    jStyle.cssSelector = Sizzle;
    jStyle.debug = true;
    jStyle.console = {
        error: function() {;
        },
        log: function() {;
        },
        info: function() {;
        },
        assert: function() {;
        },
        warn: function() {;
        },
        clear: function() {;
        }
    };
    if (jStyle.debug && typeof(console) != "undefined") jStyle.console = console;
    var __basePath = getJStyleBasePath();
    jStyle.basePath = __basePath;
    jStyle.t = function(string_id) {
        if (jStyle.lang[string_id]) return jStyle.lang[string_id];
        else return string_id;
    };
    var __xhr = function() {
        return window.ActiveXObject ? new ActiveXObject("Microsoft.XMLHTTP") : new XMLHttpRequest();
    };
    jStyle.ajax = function(options) {
        if (" " > options.url) return null;
        var xhr = __xhr();
        var complete = false;
        var success_handler = null;
        var error_handler = null;
        var type = "GET";
        var callback = function() {
            if (complete) {
                return;
            }
            if (xhr.readyState == 4) {
                if (xhr.status == 200) {
                    if (success_handler) success_handler(xhr.responseText, xhr.statusText);
                    complete = true;
                } else {
                    if (error_handler) error_handler(xhr, xhr.statusText);
                }
                xhr = null;
            } else {;
            }
        };
        if (typeof(options.success) == "function") success_handler = options.success;
        if (typeof(options.error) == "function") error_handler = options.error;
        if (" " < options.type) type = options.type;
        if (options.cache === false && type == "GET") {
            var ts = +new Date();
            var ret = options.url.replace(/(\?|&)_=.*?(&|$)/, "$1_=" + ts + "$2");
            options.url = ret + ((ret == options.url) ? (options.url.match(/\?/) ? "&": "?") + "_=" + ts: "");
        }
        if (jStyle.browser.msie) xhr.onreadystatechange = callback;
        else xhr.onload = callback;
        xhr.open(type, options.url, options.async);
        xhr.send(null);
        return xhr;
    };
    jStyle.closureListener = function(func, thisObject) {
        toArray = function(iterable) {
            var length = iterable.length,
            results = new Array(length);
            while (length--) {
                results[length] = iterable[length];
            }
            results.shift();
            return results;
        };
        var __method = func,
        args = toArray(arguments),
        object = args.shift();
        return function(e) {
            e = e || window.event;
            if (e.target) {
                var target = e.target;
            } else {
                var target = e.srcElement;
            }
            return __method.apply(object, [e, target].concat(args));
        };
    };
    var __userAgent = navigator.userAgent.toLowerCase();
    var __browser = {
        version: (__userAgent.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [0, '0'])[1],
        safari: /webkit/.test(__userAgent),
        opera: /opera/.test(__userAgent),
        msie: /msie/.test(__userAgent) && !/opera/.test(__userAgent),
        mozilla: /mozilla/.test(__userAgent) && !/(compatible|webkit)/.test(__userAgent),
        userAgent: __userAgent
    };
    jStyle.browser = __browser;
    var __imported_js = [];
    jStyle.importFile = function(path, type, charset) {
        path = toAbsolutePath(jStyle.basePath, path);
        if (type == "js") {
            for (var i = 0; i < __imported_js.length; i++) {
                if (__imported_js[i] && __imported_js[i].indexOf(path) != -1) return;
            }
            var head,
            script;
            head = document.getElementsByTagName("head")[0] || document.documentElement;
            if (!/^file:/i.test(path)) {
                var script_data = "";
                var ajaxObject;
                ajaxObject = jStyle.ajax({
                    url: path,
                    success: function(date) {
                        script_data = date;
                    },
                    async: false
                });
                if (script_data && /\S/.test(script_data)) {
                    script = document.createElement("script");
                    script.type = "text/javascript";
                    script.text = script_data;
                    head.insertBefore(script, head.firstChild);
                    head.removeChild(script);
                }
            } else {
                script = document.createElement("script");
                script.type = "text/javascript";
                script.src = path;
                head.insertBefore(script, head.firstChild);
                var completed = false;
                var tryTimes = 0;
                var sleepAndCheckLoad = function(milliseconds) {
                    if (tryTimes++>10 || script.readyState == "loaded") completed = true;
                    var start = new Date().getTime();
                    for (var i = 0; i < 1e7; i++) if ((new Date().getTime() - start) > milliseconds) break;
                };
                while (!completed) sleepAndCheckLoad(1);
            }
            jStyle.console.info("imported js: " + path);
            __imported_js.push(path);
        } else if (type == "css") {
            var ls = document.getElementsByTagName("link");
            for (var i = 0; i < ls.length; i++) {
                if (ls[i].href && ls[i].href.indexOf(path) != -1) return;
            }
            var s = document.createElement("link");
            s.rel = "stylesheet";
            s.type = "text/css";
            s.href = path;
            s.disabled = false;
            var head = document.getElementsByTagName("head")[0];
            head.appendChild(s);
        }
        return true;
    };
    jStyle.language = "en";
    jStyle.lang = [];
    jStyle.changeLanguage = function(language_name) {
        jStyle.importFile("lang/" + language_name + ".js", "js");
    };
    var __uuid = +new Date();
    jStyle.getUid = function() {
        __uuid++;
        return "jStyle" + __uuid;
    };
    jStyle.basic_element_style = function() {
        this.srcElement = null;
        this.uid = "";
    };
    jStyle.basic_style_parameter = function() {
        this.name = '';
        this.title = "";
        this.builder = function(style, element) {;
        };
    };
    jStyle.basic_style = function() {
        this.name = "";
        this.disabled = false;
        this.filter = "input,textarea,select";
        this.parameters = [];
        this.getParameter = function(name) {
            var parameter = null;
            for (var i = 0; i < this.parameters.length; i++) {
                if (this.parameters[i].name == name) {
                    parameter = this.parameters[i];
                    break;
                }
            }
            return parameter;
        };
        this.addParameter = function(param) {
            if (!param) return false;
            var param_object;
            param_object = new jStyle.basic_style_parameter();
            if (typeof(param) == "object") {
                if (" " >= param.name) return false;
                extendObject(param_object, param);
            }
            if (typeof(param) == "string") {
                param_object = new jStyle.basic_style_parameter();
                param_object.name = param;
            }
            this.parameters[param_object.name] = param_object;
            if (!this.getParameter(param_object.name)) this.parameters.push(param_object);
            return true;
        };
        this.render = function(style, element) {};
        this.build_parameters = function(style, element) {
            var parameter;
            for (var parameter_name in style) {
                parameter = this.getParameter(parameter_name);
                if (parameter && parameter.builder) parameter.builder(style[parameter_name], element);
            }
        };
        this.register = function() {
            if (this.disabled) return false;
        };
    };
    jStyle.getOrCreateElementJStyle = function(element) {
        if (element["jStyle"] && typeof(element["jStyle"]) == "object") return element["jStyle"];
        var jstyle_string = element.getAttribute("jstyle");
        var returnjStyle = new jStyle.basic_element_style();
        returnjStyle.uid = jStyle.getUid();
        if (" " < jstyle_string) {
            jstyle_string = jstyle_string.replace(/(^\s*)|(\s*$)/g, "");
            if (jstyle_string.substr(0, 1) != "{") {
                jstyle_string = "{" + jstyle_string;
                jstyle_string += "}";
            }
            try {
                var tempjStyle;
                eval("tempjStyle=" + jstyle_string + ";");
            } catch(e) {
                jStyle.console.error(element.id + "'s format of " + "jStyle" + ":\n\"" + jstyle_string + "\"\n is wrong,\n please note that it shoulde be an JavaScript object's description!");
            }
            extendObject(returnjStyle, tempjStyle);
        }
        returnjStyle.srcElement = element;
        element.jStyle = returnjStyle;
        return returnjStyle;
    };
    jStyle.addEvent = function(element, event_name, handler) { (element.addEventListener) ? element.addEventListener(event_name, handler, false) : element.attachEvent("on" + event_name, handler);
    };
    jStyle.cancelEvent = function(event) {
        if (window.event) {
            event.returnValue = false;
        } else event.preventDefault();
    };
    jStyle.createElement = function(jStyleId, tag_name) {
        if (" " > jStyleId) return null;
        var element = document.getElementById(jStyleId);
        if (element) return element;
        element = document.createElement(tag_name);
        element.id = jStyleId;
        return element;
    };
    jStyle.deleteElement = function(jStyleId) {
        var element = document.getElementById(jStyleId);
        if (element) {
            element.parentNode.removeChild(element);
            return true;
        }
        return false;
    };
    jStyle.addClass = function(element, classNames) {
        if (!element || element.nodeType != 1) return false;
        var classes = (classNames || "").split(/\s+/);
        var bAdd;
        for (var i = 0; i < classes.length; i++) {
            var elementClasses = (element.className || "").split(/\s+/);
            bAdd = true;
            for (var j = 0; j < elementClasses.length; j++) {
                if (elementClasses[j] == classes[i]) {
                    bAdd = false;
                    break;
                }
            }
            if (bAdd) element.className += (element.className ? " ": "") + classes[i];
        }
    };
    jStyle.removeClass = function(element, classNames) {
        if (!element || element.nodeType != 1) return false;
        var classes = (classNames || "").split(/\s+/);
        var bDelete;
        for (var i = 0; i < classes.length; i++) {
            var elementClasses = (element.className || "").split(/\s+/);
            bDelete = false;
            for (var j = 0; j < elementClasses.length; j++) {
                if (elementClasses[j] == classes[i]) {
                    bDelete = true;
                    break;
                }
            }
            if (bDelete) {
                if (element.className.indexOf(classes[i]) == 0) element.className = element.className.substr(classes[i].length);
                else element.className = element.className.replace(" " + classes[i], "");
            }
        }
    };
    jStyle.styles = [];
    jStyle.getStyle = function(styleName) {
        if (!styleName || styleName == "") return null;
        return jStyle.styles[styleName];
    };
    jStyle.addStyle = function(style) {
        if (!style) return false;
        var style_object;
        style_object = new jStyle.basic_style();
        if (typeof(style) == "object") {
            if (" " >= style.name) return false;
            extendObject(style_object, style);
            if (style_object.parameters && style_object.parameters.length) {
                for (var i = 0; i < style_object.parameters.length; i++) {
                    if (" " < style_object.parameters[i].name) {
                        style_object.addParameter(style_object.parameters[i]);
                    }
                }
            }
        }
        if (typeof(style) == "string") {
            style_object.name = style;
        }
        jStyle.styles.push(style_object);
        jStyle.styles[style_object.name] = style_object;
        return true;
    };
    jStyle.addStyle("css");
    jStyle.styles.css.filter = "*";
    jStyle.styles.css.render = function(style, element) {
        switch (typeof(style)) {
        case "object":
            for (var style_name in style) {
                try {
                    element.style[style_name] = style[style_name]
                } catch(e) {;
                };
            }
            delete element.jStyle.css;
            break;
        case "string":
            if (" " > style) return false;
            var style_paths = style.split(".");
            if (style_paths.length != 2) return false;
            var style_name = style_paths[1];
            try {
                element.style[style_name] = style
            } catch(e) {;
            };
            break;
        default:
            return false;
        }
    };
    jStyle.addStyle("class");
    jStyle.styles["class"].filter = "*";
    jStyle.styles["class"].render = function(style, element) {
        if (typeof(style) != "string") return false;
        var add_classes = style.match(/^\+(.*)$/);
        if (add_classes) {
            jStyle.addClass(element, add_classes[1]);
            return true;
        }
        var delete_classes = style.match(/^\-(.*)$/);
        if (delete_classes) {
            jStyle.removeClass(element, delete_classes[1]);
            return true;
        }
        element.className = style;
        return true;
    };
    jStyle.addStyle("alert");
    jStyle.styles.alert.filter = "*";
    jStyle.styles.alert.render = function(style, element) {
        var element_msg_id = element.jStyle.uid + "_" + "alert";
        var alertSpan = document.getElementById(element_msg_id);
        if (!alertSpan && style) {
            alertSpan = jStyle.createElement(element_msg_id, "span");
            alertSpan.id = element_msg_id;
            alertSpan.className = "jstyle_alert";
            element.insertAdjacentElement("afterEnd", alertSpan);
        }
        if (alertSpan && !style) jStyle.deleteElement(element_msg_id);
        if (style) alertSpan.innerHTML = style;
    };
    jStyle.addStyle("message");
    jStyle.styles.message.filter = "*";
    jStyle.styles.message.render = function(style, element) {
        var element_text_id = element.jStyle.uid + "_" + "message";
        var textSpan = document.getElementById(element_text_id);
        if (!textSpan && style) {
            textSpan = jStyle.createElement(element_text_id, "span");
            textSpan.id = element_text_id;
            textSpan.className = "jstyle_message";
            element.insertAdjacentElement("afterEnd", textSpan);
        }
        if (textSpan && !style) jStyle.deleteElement(element_text_id);
        if (style) textSpan.innerHTML = style;
    };
    jStyle.addStyle({
        name: 'validation',
        filter: "form input,textarea,select",
        parameters: [{
            name: 'required',
            message: jStyle.t("Required"),
            title: jStyle.t("Required Item"),
            checker: function(element, setting) {
                if ("" < element.value) return true;
            },
            builder: function(style, element) {
                var element_id = element.jStyle.uid + "_validation_required";
                var redStarNode = document.getElementById(element_id);
                if (!redStarNode && style) {
                    var redStarNode = jStyle.createElement(element_id, "span");
                    redStarNode.className = "validation_required";
                    redStarNode.id = element_id;
                    element.insertAdjacentElement("afterEnd", redStarNode);
                }
                if (redStarNode && style) redStarNode.title = this.title;
                if (redStarNode && !style) jStyle.deleteElement(element_id);
            }
        },
        {
            name: 'min_length',
            message: jStyle.t("The littler value is $0"),
            checker: function(element, setting) {
                if (!setting || " " > element.value) return true;
                if (setting <= element.value.length) return true;
                return false;
            }
        },
        {
            name: 'max_length',
            message: jStyle.t("The greater value is $0"),
            checker: function(element, setting) {
                if (!setting || " " > element.value) return true;
                if (setting >= element.value.length) return true;
                return false;
            }
        },
        {
            name: 'littler',
            message: jStyle.t("More then $0"),
            checker: function(element, setting) {
                if (!setting || " " > element.value) return true;
                if (setting >= element.value) return true;
                return false;
            }
        },
        {
            name: 'greater',
            message: jStyle.t("Less then $0"),
            checker: function(element, setting) {
                if (!setting || " " > element.value) return true;
                if (setting <= element.value) return true;
                return false;
            }
        },
        {
            name: 'exact_length',
            message: jStyle.t("Length is $0"),
            checker: function(element, setting) {
                if (!setting || " " > element.value) return true;
                if (setting == element.value.length) return true;
                return false;
            }
        },
        {
            name: 'alpha',
            message: jStyle.t("Shoud be alpha"),
            checker: function(element, setting) {
                if (!setting || " " > element.value) return true;
                if (element.value.match(/^([a-z])+$/i)) return true;
                return false;
            }
        },
        {
            name: 'alpha_numeric',
            message: jStyle.t("Shoud be alpha or numeric"),
            checker: function(element, setting) {
                if (!setting || " " > element.value) return true;
                if (element.value.match(/^([a-z0-9])+$/i)) return true;
                return false;
            }
        },
        {
            name: 'numeric',
            message: jStyle.t("Shoud be numeric"),
            checker: function(element, setting) {
                if (!setting || " " > element.value) return true;
                if (element.value.match(/^[\-+]?[0-9]*\.?[0-9]+$/)) return true;
                return false;
            }
        },
        {
            name: 'integer',
            message: jStyle.t("Shoud be integer"),
            checker: function(element, setting) {
                if (!setting || " " > element.value) return true;
                if (element.value.match(/^[\-+]?[0-9]+$/i)) return true;
                return false;
            }
        },
        {
            name: 'email',
            message: jStyle.t("should be an email address"),
            checker: function(element, setting) {
                if (!setting || " " > element.value) return true;
                if (element.value.match(/^([a-z0-9\+_\-]+)(\.[a-z0-9\+_\-]+)*@([a-z0-9\-]+\.)+[a-z]{2,6}$/i)) return true;
                return false;
            }
        },
        {
            name: 'ip',
            message: jStyle.t("should be an ip string"),
            checker: function(element, setting) {
                if (!setting || " " > element.value) return true;
                var ips = element.value.split(".");
                if (ips.length != 4) {
                    return false;
                }
                for (var i = 0; i < ips.length; i++) {
                    if (ips[i] == '' || !ips[i].match(/[0-9]+$/) || parseInt(ips[i]) > 255 || ips[i].length > 3) return false;
                }
                return true;
            }
        },
        {
            name: 'base64',
            message: jStyle.t("should be an base64 string"),
            checker: function(element, setting) {
                if (!setting || " " > element.value) return true;
                if (element.value.match(/[^a-zA-Z0-9\/\+=]/i)) return true;
                return false;
            }
        },
        {
            name: 'regexp',
            message: jStyle.t("Dose not match the specific format"),
            checker: function(element, setting) {
                if (!setting || " " > element.value) return true;
                if (element.value.match(eval("/" + setting + "/i"))) return true;
                return false;
            }
        },
        {
            name: 'matches',
            message: jStyle.t("Dose not match"),
            checker: function(element, setting) {
                if (!setting) return true;
                var input_match_id,
                input_match_name,
                input_match;
                for (var i = 0; i < setting.length; i++) {
                    input_match_id = setting[i];
                    input_match = jStyle.cssSelector("#" + input_match_id, element.form)[0];
                    if (!input_match) {
                        alert(jStyle.t("Can not find the element to match ") + input_match_id);
                        return false;
                    }
                    if (element.value == input_match.value) return true;
                }
                return false;
            }
        }],
        register: function() {
            if (this.disabled) return false;
            var validation_form = function(e) {
                var bRet = true;
                var validation_elements = jStyle.cssSelector(jStyle.styles.validation.filter.replace("form ", ""), this);
                for (var i = 0; i < validation_elements.length; i++) {
                    if (validation_elements[i].jStyle && validation_elements[i].jStyle.validation) {
                        if (!jStyle.styles.validation.validater(validation_elements[i])) bRet = false;
                    }
                }
                if (!bRet) jStyle.cancelEvent(e);
                return bRet;
            };
            var forms = document.getElementsByTagName("form");
            for (var i = 0; i < forms.length; i++) jStyle.addEvent(forms[i], 'submit', jStyle.closureListener(validation_form, forms[i]));
        },
        validater: function(element) {
            var bRet = true;
            var validation_style = element.jStyle.validation;
            if (!validation_style) {
                alert("Error!element " + this.id + " should has validation jstyle!");
                return false;
            }
            var element_name;
            if (validation_style.name) element_name = validation_style.name;
            else element_name = element.id;
            jStyle(element, "alert", "");
            var message = "";
            for (var i = 0; i < this.parameters.length; i++) {
                if (validation_style[this.parameters[i].name] && this.parameters[i].checker) {
                    if (!this.parameters[i].checker(element, validation_style[this.parameters[i].name])) {
                        message = "";
                        if (" " < this.parameters[i].message) message = this.parameters[i].message;
                        if (" " < validation_style[this.parameters[i].name + "_message"]) message = validation_style[this.parameters[i].name + "_message"];
                        message = message.replace("$0", validation_style[this.parameters[i].name]);
                        if (" " < message) jStyle(element, "alert", element_name + " " + message);
                        return false;
                    }
                }
            }
            return bRet;
        }
    });
    jStyle.addStyle({
        name: 'render',
        filter: "input,textarea,select",
        parameters: [{
            name: 'dialog',
            title: "",
            render_dialog: function(e) {
                var returnV;
                var render_dialog;
                if (this.jStyle.render && this.jStyle.render.dialog) render_dialog = this.jStyle.render.dialog;
                if (!render_dialog) {
                    alert("Error!element " + this.id + " should has render.dialog jstyle!");
                    return false;
                }
                var vArguments = new Object();
                vArguments.text = (this.jStyle.render.dialog.text) ? this.jStyle.render.dialog.text: "";
                vArguments.value = this.value;
                var dialog_features = "";
                render_dialog.url = render_dialog.url + ((render_dialog.url.match(/\?/)) ? "": "?") + "&value=" + vArguments.value + "&text=" + vArguments.text;
                if (render_dialog.features) dialog_features = render_dialog.features;
                returnV = window.showModalDialog(render_dialog.url, vArguments, dialog_features);
                if (returnV) {
                    this.value = vArguments.value;
                    jStyle(this, "message", vArguments.text);
                }
                return returnV;
            },
            builder: function(style, element) {
                var element_id = element.jStyle.uid + "_" + "render_dialog";
                var button = document.getElementById(element_id);
                var title = "";
                if (element.jStyle.render.dialog.title) title = element.jStyle.render.dialog.title;
                var text = "";
                if (element.jStyle.render.dialog.text) text = element.jStyle.render.dialog.text;
                if (!button && style) {
                    button = jStyle.createElement(element_id, "span");
                    button.id = element_id;
                    button.className = "render_browser";
                    button.onclick = jStyle.closureListener(this.render_dialog, element);
                    element.insertAdjacentElement("afterEnd", button);
                    element.style.display = "none";
                }
                if (button && style) {
                    jStyle(element, "message", text);
                    button.innerHTML = title;
                    button.title = jStyle.t("Brower...");
                }
                if (button && !style) {
                    element.style.display = "";
                    jStyle.deleteElement(button);
                }
            }
        },
        {
            name: 'validation',
            title: "",
            render_validation: function(e) {
                var bRet = false;
                if (" " > this.value) return true;
                var render_validation;
                if (this.jStyle.render && this.jStyle.render.validation) render_validation = this.jStyle.render.validation;
                if (!render_validation) {
                    alert("Error!element " + this.id + " should has render.validation jstyle!");
                    return false;
                }
                var url = render_validation.url + (render_validation.url.match(/\?/) ? "&": "?") + "value=" + this.value;
                var ajax = jStyle.ajax({
                    type: "POST",
                    url: url,
                    async: false,
                    success: function(text) {
                        if (text == "1") bRet = true;
                    }
                });
                var title = jStyle.t("Validate");
                title += " ";
                var message = "";
                if (render_validation.title) title = render_validation.title;
                if (bRet) {
                    if (render_validation.success_message) message = render_validation.success_message;
                    else message = title + jStyle.t("Success")
                } else {
                    if (render_validation.failure_message) message = render_validation.failure_message;
                    else message = title + jStyle.t("Failed")
                }
                jStyle(this, "alert", message);
                return bRet;
            },
            builder: function(style, element) {
                var button_id = element.jStyle.uid + "_render_validation";
                var button = document.getElementById(button_id);
                var title = "";
                if (element.jStyle.render.validation.title) title = element.jStyle.render.validation.title;
                if (!button && style) {
                    button = jStyle.createElement(button_id, "span");
                    button.className = "render_validation";
                    button.id = button_id;
                    button.onclick = jStyle.closureListener(this.render_validation, element);
                    element.insertAdjacentElement("afterEnd", button);
                }
                if (button && style) {
                    var title = jStyle.t("Validate");;
                    if (style.title) title = style.title;
                    button.title = title;
                    button.innerHTML = title;
                }
                if (button && !style) jStyle.deleteElement(button_id);
            }
        },
        {
            name: 'getvalue',
            title: "",
            render_getvalue: function(e) {
                var sRet = "";
                var render_getvalue;
                if (this.jStyle.render && this.jStyle.render.getvalue) render_getvalue = this.jStyle.render.getvalue;
                if (!render_getvalue) {
                    alert("Error!element " + this.id + " should has render.getvalue jstyle!");
                    return false;
                }
                var url = render_getvalue.url + (render_getvalue.url.match(/\?/) ? "&": "?") + "value=" + this.value;
                jStyle.ajax({
                    type: "POST",
                    url: url,
                    async: false,
                    success: function(text) {
                        if (text.length < "200") sRet = text;
                    }
                });
                if (" " < sRet) this.value = sRet;
                return true;
            },
            builder: function(style, element) {
                var element_id = element.jStyle.uid + "_render_getvalue";
                var button = document.getElementById(element_id);
                if (!button && style) {
                    button = jStyle.createElement(element_id, "span");
                    button.className = "render_getvalue";
                    button.id = element_id;
                    button.onclick = jStyle.closureListener(this.render_getvalue, element);
                    element.insertAdjacentElement("afterEnd", button);
                    if (style.readonly) element.readOnly = true;
                }
                if (button && style) {
                    var title = jStyle.t("Get value");
                    if (style.title) title = style.title;
                    button.title = title;
                    button.innerHTML = title;
                }
                if (button && !style) {
                    jStyle.deleteElement(element_id);
                    element.readOnly = false;
                }
            }
        }],
        render: function(style, element) {;
        }
    });
})();
jStyle.addEvent(window, "load", jStyle.loader);