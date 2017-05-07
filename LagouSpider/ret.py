"object"!=typeof JSON2&&(JSON2=window.JSON||{}),

function(){"use strict";function f(e){return e<10?"0"+e:e}

function objectToJSON(e,t){var n=Object.prototype.toString.apply(e);return"[object Date]"===n?isFinite(e.valueOf())?e.getUTCFullYear()+"-"+f(e.getUTCMonth()+1)+"-"+f(e.getUTCDate())+"T"+f(e.getUTCHours())+":"+f(e.getUTCMinutes())+":"+f(e.getUTCSeconds())+"Z":null:"[object String]"===n||"[object Number]"===n||"[object Boolean]"===n?e.valueOf():"[object Array]"!==n&&"function"==typeof e.toJSON?e.toJSON(t):e}

function quote(e){return escapable.lastIndex=0,escapable.test(e)?'"'+e.replace(escapable,function(e){var t=meta[e];return"string"==typeof t?t:"\\u"+("0000"+e.charCodeAt(0).toString(16)).slice(-4)})+'"':'"'+e+'"'}function str(e,t){var n,i,r,o,a,s=gap,c=t[e];switch(c&&"object"==typeof c&&(c=objectToJSON(c,e)),"function"==typeof rep&&(c=rep.call(t,e,c)),typeof c){case"string":return quote(c);case"number":return isFinite(c)?String(c):"null";case"boolean":case"null":return String(c);case"object":if(!c)return"null";if(gap+=indent,a=[],"[object Array]"===Object.prototype.toString.apply(c)){for(o=c.length,n=0;n<o;n+=1)a[n]=str(n,c)||"null";return r=0===a.length?"[]":gap?"[\n"+gap+a.join(",\n"+gap)+"\n"+s+"]":"["+a.join(",")+"]",gap=s,r}if(rep&&"object"==typeof rep)for(o=rep.length,n=0;n<o;n+=1)"string"==typeof rep[n]&&(i=rep[n],r=str(i,c),r&&a.push(quote(i)+(gap?": ":":")+r));else for(i in c)Object.prototype.hasOwnProperty.call(c,i)&&(r=str(i,c),r&&a.push(quote(i)+(gap?": ":":")+r));return r=0===a.length?"{}":gap?"{\n"+gap+a.join(",\n"+gap)+"\n"+s+"}":"{"+a.join(",")+"}",gap=s,r}}var cx=new RegExp("[\0\xad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]","g"),pattern='\\\\\\"\0-\x1f\x7f-\x9f\xad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]',escapable=new RegExp("["+pattern,"g"),gap,indent,meta={"\b":"\\b","\t":"\\t","\n":"\\n","\f":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"},rep;"function"!=typeof JSON2.stringify&&(JSON2.stringify=function(e,t,n){var i;if(gap="",indent="","number"==typeof n)for(i=0;i<n;i+=1)indent+=" ";else"string"==typeof n&&(indent=n);if(rep=t,t&&"function"!=typeof t&&("object"!=typeof t||"number"!=typeof t.length))throw new Error("JSON2.stringify");return str("",{"":e})}),"function"!=typeof JSON2.parse&&(JSON2.parse=function(text,reviver){function walk(e,t){var n,i,r=e[t];if(r&&"object"==typeof r)for(n in r)Object.prototype.hasOwnProperty.call(r,n)&&(i=walk(r,n),void 0!==i?r[n]=i:delete r[n]);return reviver.call(e,t,r)}var j;if(text=String(text),cx.lastIndex=0,cx.test(text)&&(text=text.replace(cx,function(e){return"\\u"+("0000"+e.charCodeAt(0).toString(16)).slice(-4)})),new RegExp("^[\\],:{}\\s]*$").test(text.replace(new RegExp('\\\\(?:["\\\\/bfnrt]|u[0-9a-fA-F]{4})',"g"),"@").replace(new RegExp('"[^"\\\\\n\r]*"|true|false|null|-?\\d+(?:\\.\\d*)?(?:[eE][+\\-]?\\d+)?',"g"),"]").replace(new RegExp("(?:^|:|,)(?:\\s*\\[)+","g"),"")))return j=eval("("+text+")"),"function"==typeof reviver?walk({"":j},""):j;throw new SyntaxError("JSON2.parse")})}(),"object"!=typeof _jzlpaq&&(_jzlpaq=[]),"object"!=typeof JZLPiwik&&(JZLPiwik=function(){"use strict";function isDefined(e){var t=typeof e;return"undefined"!==t}

	function isFunction(e){return"function"==typeof e}

	function isObject(e){return"object"==typeof e}

	function isString(e){return"string"==typeof e||e instanceof String}

	function apply(){var e,t,n;for(e=0;e<arguments.length;e+=1)n=arguments[e],t=n.shift(),isString(t)?asyncTracker[t].apply(asyncTracker,n):t.apply(asyncTracker,n)}

	function addEventListener(e,t,n,i){return e.addEventListener?(e.addEventListener(t,n,i),!0):e.attachEvent?e.attachEvent("on"+t,n):void(e["on"+t]=n)}

	function executePluginMethod(e,t){var n,i,r="";for(n in plugins)Object.prototype.hasOwnProperty.call(plugins,n)&&(i=plugins[n][e],isFunction(i)&&(r+=i(t)));return r}

	function beforeUnloadHandler(){var e;if(executePluginMethod("unload"),expireDateTime)do e=new Date;while(e.getTimeAlias()<expireDateTime)}

	function loadHandler(){var e;if(!hasLoaded)for(hasLoaded=!0,executePluginMethod("load"),e=0;e<registeredOnLoadHandlers.length;e++)registeredOnLoadHandlers[e]();return!0}

	function addReadyListener(){var e;documentAlias.addEventListener?addEventListener(documentAlias,"DOMContentLoaded",function t(){documentAlias.removeEventListener("DOMContentLoaded",t,!1),loadHandler()}):documentAlias.attachEvent&&(documentAlias.attachEvent("onreadystatechange",function n(){"complete"===documentAlias.readyState&&(documentAlias.detachEvent("onreadystatechange",n),loadHandler())}),documentAlias.documentElement.doScroll&&windowAlias===windowAlias.top&&!function i(){if(!hasLoaded){try{documentAlias.documentElement.doScroll("left")}catch(e){return void setTimeout(i,0)}loadHandler()}}()),new RegExp("WebKit").test(navigatorAlias.userAgent)&&(e=setInterval(function(){(hasLoaded||/loaded|complete/.test(documentAlias.readyState))&&(clearInterval(e),loadHandler())},10)),addEventListener(windowAlias,"load",loadHandler,!1)}

	function loadScript(e,t){var n=documentAlias.createElement("script");n.type="text/javascript",n.src=e,n.readyState?n.onreadystatechange=function(){var e=this.readyState;"loaded"!==e&&"complete"!==e||(n.onreadystatechange=null,t())}:n.onload=t,documentAlias.getElementsByTagName("head")[0].appendChild(n)}

	function getReferrer(){var e="";try{var t=windowAlias.location.href;e=""!=getParameter(t,"jzl_ref")&&void 0!=getParameter(t,"jzl_ref")?getParameter(t,"jzl_ref"):windowAlias.top.document.referrer}catch(n){if(windowAlias.parent)try{e=windowAlias.parent.document.referrer}catch(i){e=""}}return""===e&&(e=documentAlias.referrer),e}

	function getProtocolScheme(e){var t=new RegExp("^([a-z]+):"),n=t.exec(e);return n?n[1]:null}

	function getHostName(e){var t=new RegExp("^(?:(?:https?|ftp):)/*(?:[^@]+@)?([^:/#]+)"),n=t.exec(e);return n?n[1]:e}

	function getParameter(e,t){var n="[\\?&#]"+t+"=([^&#]*)",i=new RegExp(n),r=i.exec(e);return r?decodeWrapper(r[1]):""}

	function utf8_encode(e){return unescape(encodeWrapper(e))}

	function sha1(e){var t,n,i,r,o,a,s,c,u,l,f=function(e,t){return e<<t|e>>>32-t},d=function(e){var t,n,i="";for(t=7;t>=0;t--)n=e>>>4*t&15,i+=n.toString(16);return i},g=[],m=1732584193,p=4023233417,h=2562383102,k=271733878,v=3285377520,T=[];for(e=utf8_encode(e),l=e.length,n=0;n<l-3;n+=4)i=e.charCodeAt(n)<<24|e.charCodeAt(n+1)<<16|e.charCodeAt(n+2)<<8|e.charCodeAt(n+3),T.push(i);switch(3&l){case 0:n=2147483648;break;case 1:n=e.charCodeAt(l-1)<<24|8388608;break;case 2:n=e.charCodeAt(l-2)<<24|e.charCodeAt(l-1)<<16|32768;break;case 3:n=e.charCodeAt(l-3)<<24|e.charCodeAt(l-2)<<16|e.charCodeAt(l-1)<<8|128}for(T.push(n);14!==(15&T.length);)T.push(0);for(T.push(l>>>29),T.push(l<<3&4294967295),t=0;t<T.length;t+=16){for(n=0;n<16;n++)g[n]=T[t+n];for(n=16;n<=79;n++)g[n]=f(g[n-3]^g[n-8]^g[n-14]^g[n-16],1);for(r=m,o=p,a=h,s=k,c=v,n=0;n<=19;n++)u=f(r,5)+(o&a|~o&s)+c+g[n]+1518500249&4294967295,c=s,s=a,a=f(o,30),o=r,r=u;for(n=20;n<=39;n++)u=f(r,5)+(o^a^s)+c+g[n]+1859775393&4294967295,c=s,s=a,a=f(o,30),o=r,r=u;for(n=40;n<=59;n++)u=f(r,5)+(o&a|o&s|a&s)+c+g[n]+2400959708&4294967295,c=s,s=a,a=f(o,30),o=r,r=u;for(n=60;n<=79;n++)u=f(r,5)+(o^a^s)+c+g[n]+3395469782&4294967295,c=s,s=a,a=f(o,30),o=r,r=u;m=m+r&4294967295,p=p+o&4294967295,h=h+a&4294967295,k=k+s&4294967295,v=v+c&4294967295}return u=d(m)+d(p)+d(h)+d(k)+d(v),u.toLowerCase()}

	function urlFixup(e,t,n){return"translate.googleusercontent.com"===e?(""===n&&(n=t),t=getParameter(t,"u"),e=getHostName(t)):"cc.bingj.com"!==e&&"webcache.googleusercontent.com"!==e&&"74.6."!==e.slice(0,5)||(t=documentAlias.links[0].href,e=getHostName(t)),[e,t,n]}

	function domainFixup(e){var t=e.length;return"."===e.charAt(--t)&&(e=e.slice(0,t)),"*."===e.slice(0,2)&&(e=e.slice(1)),e}

	function titleFixup(e){if(e=e&&e.text?e.text:e,!isString(e)){var t=documentAlias.getElementsByTagName("title");t&&isDefined(t[0])&&(e=t[0].text)}return e}

	 getChildrenFromNode(e){return e?!isDefined(e.children)&&isDefined(e.childNodes)?e.children:isDefined(e.children)?e.children:[]:[]}

	 function containsNodeElement(e,t){return!(!e||!t)&&(e.contains?e.contains(t):e===t||!!e.compareDocumentPosition&&!!(16&e.compareDocumentPosition(t)))}

	 function indexOfArray(e,t){if(e&&e.indexOf)return e.indexOf(t);if(!isDefined(e)||null===e)return-1;if(!e.length)return-1;var n=e.length;if(0===n)return-1;for(var i=0;i<n;){if(e[i]===t)return i;i++}return-1}

	 function isVisible(e){function t(e,t){return windowAlias.getComputedStyle?documentAlias.defaultView.getComputedStyle(e,null)[t]:e.currentStyle?e.currentStyle[t]:void 0}

	 function n(e){for(e=e.parentNode;e;){if(e===documentAlias)return!0;e=e.parentNode}return!1}

	 function i(r,o,a,s,c,u,l){var f=r.parentNode,d=1;return!!n(r)&&(9===f.nodeType||"0"!==t(r,"opacity")&&"none"!==t(r,"display")&&"hidden"!==t(r,"visibility")&&(isDefined(o)&&isDefined(a)&&isDefined(s)&&isDefined(c)&&isDefined(u)&&isDefined(l)||(o=r.offsetTop,c=r.offsetLeft,s=o+r.offsetHeight,a=c+r.offsetWidth,u=r.offsetWidth,l=r.offsetHeight),(e!==r||0!==l&&0!==u||"hidden"!==t(r,"overflow"))&&(!f||("hidden"!==t(f,"overflow")&&"scroll"!==t(f,"overflow")||!(c+d>f.offsetWidth+f.scrollLeft||c+u-d<f.scrollLeft||o+d>f.offsetHeight+f.scrollTop||o+l-d<f.scrollTop))&&(r.offsetParent===f&&(c+=f.offsetLeft,o+=f.offsetTop),i(f,o,a,s,c,u,l)))))}return!!e&&i(e)}

	 function getPiwikUrlForOverlay(e,t){return t?t:("piwik.php"===e.slice(-9)&&(e=e.slice(0,e.length-9)),e)}

	 function isOverlaySession(e){var t="Piwik_Overlay",n=new RegExp("index\\.php\\?module=Overlay&action=startOverlaySession&idSite=([0-9]+)&period=([^&]+)&date=([^&]+)$"),i=n.exec(documentAlias.referrer);if(i){var r=i[1];if(r!==String(e))return!1;var o=i[2],a=i[3];windowAlias.name=t+"###"+o+"###"+a}var s=windowAlias.name.split("###");return 3===s.length&&s[0]===t}

	 function injectOverlayScripts(e,t,n){var i=windowAlias.name.split("###"),r=i[1],o=i[2],a=getPiwikUrlForOverlay(e,t);loadScript(a+"plugins/Overlay/client/client.js?v=1",function(){Piwik_Overlay_Client.initialize(a,n,r,o)})}

	 function EventMap(){var e=0,t=new Object;this.put=function(n,i){null!=n&&null!=i&&(t[n]=i,e++)},this.append=function(n,i){if(null!=n&&null!=i)if(null==t[n]){var r=new Array;r.push(i),t[n]=r,e++}else{var r=t[n];r.push(i),t[n]=r}},this.clear=function(){e=0,t=new Object},this.get=function(e){return null==t[e]?null:t[e]},this.remove=function(n){null!=t[n]&&(t[n]=null,e--)},this.size=function(){return e},this.toString=function(){var e="[";for(var n in t)e+=n,e+=t[n],e+=",";return","===e.charAt(e.length-1)&&(e=e.substring(0,e.length-1)),e+="]"}}

	 function Tracker(trackerUrl,siteId){function setCookie(e,t,n,i,r,o){if(!configCookiesDisabled){var a;n&&(a=new Date,a.setTime(a.getTime()+n)),documentAlias.cookie=e+"="+encodeWrapper(t)+(n?";expires="+a.toGMTString():"")+";path="+(i||"/")+(r?";domain="+r:"")+(o?";secure":"")}}

	 function getCookie(e){if(configCookiesDisabled)return 0;var t=new RegExp("(^|;)[ ]*"+e+"=([^;]*)"),n=t.exec(documentAlias.cookie);return n?decodeWrapper(n[2]):0}

	 function purify(e){var t;return configDiscardHashTag?(t=new RegExp("#.*"),e.replace(t,"")):e}

	 function resolveRelativeReference(e,t){var n,i=getProtocolScheme(t);return i?t:"/"===t.slice(0,1)?getProtocolScheme(e)+"://"+getHostName(e)+t:(e=purify(e),n=e.indexOf("?"),n>=0&&(e=e.slice(0,n)),n=e.lastIndexOf("/"),n!==e.length-1&&(e=e.slice(0,n+1)),e+t)}

	 function isSiteHostName(e){var t,n,i;for(t=0;t<configHostsAlias.length;t++){if(n=domainFixup(configHostsAlias[t].toLowerCase()),e===n)return!0;if("."===n.slice(0,1)){if(e===n.slice(1))return!0;if(i=e.length-n.length,i>0&&e.slice(i)===n)return!0}}return!1}

	 function getImage(e,t){var n=new Image(1,1);n.onload=function(){iterator=0,"function"==typeof t&&t()},n.src=configTrackerUrl+(configTrackerUrl.indexOf("?")<0?"?":"&")+e}

	 function sendXmlHttpRequest(e,t,n){isDefined(n)&&null!==n||(n=!0);try{var i=windowAlias.XMLHttpRequest?new windowAlias.XMLHttpRequest:windowAlias.ActiveXObject?new ActiveXObject("Microsoft.XMLHTTP"):null;i.open("POST",configTrackerUrl,!0),i.onreadystatechange=function(){4!==this.readyState||this.status>=200&&this.status<300||!n?"function"==typeof t&&t():getImage(e,t)},i.setRequestHeader("Content-Type",configRequestContentType),i.send(e)}catch(r){n&&getImage(e,t)}}

	 function setExpireDateTime(e){var t=new Date,n=t.getTime()+e;(!expireDateTime||n>expireDateTime)&&(expireDateTime=n)}

	 function heartBeatUp(e){!heartBeatTimeout&&configHeartBeatDelay&&(heartBeatTimeout=setTimeout(function(){if(heartBeatTimeout=null,!heartBeatPingIfActivityAlias()){var e=new Date,t=configHeartBeatDelay-(e.getTime()-lastTrackerRequestTime);t=Math.min(configHeartBeatDelay,t),heartBeatUp(t)}},e||configHeartBeatDelay))}

	 function heartBeatDown(){heartBeatTimeout&&(clearTimeout(heartBeatTimeout),heartBeatTimeout=null)}

	 function heartBeatOnFocus(){heartBeatPingIfActivityAlias()||heartBeatUp()}

	 function heartBeatOnBlur(){heartBeatDown()}function setUpHeartBeat(){!heartBeatSetUp&&configHeartBeatDelay&&(heartBeatSetUp=!0,addEventListener(windowAlias,"focus",heartBeatOnFocus),addEventListener(windowAlias,"blur",heartBeatOnBlur),heartBeatUp())}

	 function makeSureThereIsAGapAfterFirstTrackingRequestToPreventMultipleVisitorCreation(e){var t=new Date,n=t.getTime();if(lastTrackerRequestTime=n,timeNextTrackingRequestCanBeExecutedImmediately&&n<timeNextTrackingRequestCanBeExecutedImmediately){var i=timeNextTrackingRequestCanBeExecutedImmediately-n;return setTimeout(e,i),setExpireDateTime(i+50),void(timeNextTrackingRequestCanBeExecutedImmediately+=50)}if(timeNextTrackingRequestCanBeExecutedImmediately===!1){var r=800;timeNextTrackingRequestCanBeExecutedImmediately=n+r}e()}

	 function sendRequest(e,t,n){!configDoNotTrack&&e&&makeSureThereIsAGapAfterFirstTrackingRequestToPreventMultipleVisitorCreation(function(){"POST"===configRequestMethod?sendXmlHttpRequest(e,n):getImage(e,n),setExpireDateTime(t)}),heartBeatSetUp?heartBeatUp():setUpHeartBeat()}

	 function canSendBulkRequest(e){return!configDoNotTrack&&(e&&e.length)}

	 function sendBulkRequest(e,t){if(canSendBulkRequest(e)){var n='{"requests":["?'+e.join('","?')+'"]}';makeSureThereIsAGapAfterFirstTrackingRequestToPreventMultipleVisitorCreation(function(){sendXmlHttpRequest(n,null,!1),setExpireDateTime(t)})}}

	 function getCookieName(e){return configCookieNamePrefix+e+"."+configTrackerSiteId+"."+domainHash}

	 function hasCookies(){if(configCookiesDisabled)return"0";if(!isDefined(navigatorAlias.cookieEnabled)){var e=getCookieName("testcookie");return setCookie(e,"1"),"1"===getCookie(e)?"1":"0"}return navigatorAlias.cookieEnabled?"1":"0"}

	 function updateDomainHash(){domainHash=hash((configCookieDomain||domainAlias)+(configCookiePath||"/")).slice(0,4)}

	 function getCustomVariablesFromCookie(){var e=getCookieName("cvar"),t=getCookie(e);return t.length&&(t=JSON2.parse(t),isObject(t))?t:{}}

	 function loadCustomVariables(){customVariables===!1&&(customVariables=getCustomVariablesFromCookie())}

	 function generateRandomUuid(){return hash((navigatorAlias.userAgent||"")+(navigatorAlias.platform||"")+JSON2.stringify(browserFeatures)+(new Date).getTime()+Math.random()).slice(0,16)}

	 function loadVisitorIdCookie(){var e,t,n=new Date,i=Math.round(n.getTime()/1e3),r=getCookieName("id"),o=getCookie(r);return o?(e=o.split("."),e.unshift("0"),visitorUUID.length&&(e[1]=visitorUUID),e):(t=visitorUUID.length?visitorUUID:"0"===hasCookies()?"":generateRandomUuid(),e=["1",t,i,0,i,"",""])}

	 function getValuesFromVisitorIdCookie(){var e=loadVisitorIdCookie(),t=e[0],n=e[1],i=e[2],r=e[3],o=e[4],a=e[5];isDefined(e[6])||(e[6]="");var s=e[6];return{newVisitor:t,uuid:n,createTs:i,visitCount:r,currentVisitTs:o,lastVisitTs:a,lastEcommerceOrderTs:s}}

	 function getRemainingVisitorCookieTimeout(){var e=new Date,t=e.getTime(),n=getValuesFromVisitorIdCookie().createTs,i=parseInt(n,10),r=1e3*i+configVisitorCookieTimeout-t;return r}

	 function setVisitorIdCookie(e){if(configTrackerSiteId){var t=new Date,n=Math.round(t.getTime()/1e3);isDefined(e)||(e=getValuesFromVisitorIdCookie());var i=e.uuid+"."+e.createTs+"."+e.visitCount+"."+n+"."+e.lastVisitTs+"."+e.lastEcommerceOrderTs;setCookie(getCookieName("id"),i,getRemainingVisitorCookieTimeout(),configCookiePath,configCookieDomain)}}

	 function loadReferrerAttributionCookie(){var e=getCookie(getCookieName("ref"));if(e.length)try{if(e=JSON2.parse(e),isObject(e))return e}catch(t){}return["","",0,""]}

	 function deleteCookie(e,t,n){setCookie(e,"",-86400,t,n)}

	 function isPossibleToSetCookieOnDomain(e){var t="testvalue";return setCookie("test",t,1e4,null,e),getCookie("test")===t&&(deleteCookie("test",null,e),!0)}

	 function deleteCookies(){var e=configCookiesDisabled;configCookiesDisabled=!1;var t,n,i=["id","ses","cvar","ref"];for(t=0;t<i.length;t++)n=getCookieName(i[t]),0!==getCookie(n)&&deleteCookie(n,configCookiePath,configCookieDomain);configCookiesDisabled=e}

	 function setSiteId(e){configTrackerSiteId=e,setVisitorIdCookie()}

	 function sortObjectByKeys(e){if(e&&isObject(e)){var t,n=[];for(t in e)Object.prototype.hasOwnProperty.call(e,t)&&n.push(t);var i={};n.sort();var r,o=n.length;for(r=0;r<o;r++)i[n[r]]=e[n[r]];return i}}

	 function setSessionCookie(){setCookie(getCookieName("ses"),"*",configSessionCookieTimeout,configCookiePath,configCookieDomain)}

	 function getRequest(e,t,n,i){function r(e,t){var n=JSON2.stringify(e);return n.length>2?"&"+t+"="+encodeWrapper(n):""}var o,a,s,c,u,l,f,d=new Date,g=Math.round(d.getTime()/1e3),m=1024,p=customVariables,h=getCookieName("ses"),k=getCookieName("ref"),v=getCookieName("cvar"),T=getCookie(h),C=loadReferrerAttributionCookie(),b=configCustomUrl||locationHrefAlias;if(configCookiesDisabled&&deleteCookies(),configDoNotTrack)return"";var y=getValuesFromVisitorIdCookie();isDefined(i)||(i="");var A=documentAlias.characterSet||documentAlias.charset;if(A&&"utf-8"!==A.toLowerCase()||(A=null),l=C[0],f=C[1],a=C[2],s=C[3],!T){var N=configSessionCookieTimeout/1e3;if((!y.lastVisitTs||g-y.lastVisitTs>N)&&(y.visitCount++,y.lastVisitTs=y.currentVisitTs),!configConversionAttributionFirstReferrer||!l.length){for(o in configCampaignNameParameters)if(Object.prototype.hasOwnProperty.call(configCampaignNameParameters,o)&&(l=getParameter(b,configCampaignNameParameters[o]),l.length))break;for(o in configCampaignKeywordParameters)if(Object.prototype.hasOwnProperty.call(configCampaignKeywordParameters,o)&&(f=getParameter(b,configCampaignKeywordParameters[o]),f.length))break}c=getHostName(configReferrerUrl),u=s.length?getHostName(s):"",!c.length||isSiteHostName(c)||configConversionAttributionFirstReferrer&&u.length&&!isSiteHostName(u)||(s=configReferrerUrl),(s.length||l.length)&&(a=g,C=[l,f,a,purify(s.slice(0,m))],setCookie(k,JSON2.stringify(C),configReferralCookieTimeout,configCookiePath,configCookieDomain))}e+="&idsite="+configTrackerSiteId+"&rec=1&r="+String(Math.random()).slice(2,8)+"&h="+d.getHours()+"&m="+d.getMinutes()+"&s="+d.getSeconds()+"&url="+encodeWrapper(purify(b))+(configReferrerUrl.length?"&urlref="+encodeWrapper(purify(configReferrerUrl)):"")+(configUserId&&configUserId.length?"&uid="+encodeWrapper(configUserId):"")+"&_id="+y.uuid+"&_idts="+y.createTs+"&_idvc="+y.visitCount+"&_idn="+y.newVisitor+(l.length?"&_rcn="+encodeWrapper(l):"")+(f.length?"&_rck="+encodeWrapper(f):"")+"&_refts="+a+"&_viewts="+y.lastVisitTs+(String(y.lastEcommerceOrderTs).length?"&_ects="+y.lastEcommerceOrderTs:"")+(String(s).length?"&_ref="+encodeWrapper(purify(s.slice(0,m))):"")+(A?"&cs="+encodeWrapper(A):"")+"&send_image=0";for(o in browserFeatures)Object.prototype.hasOwnProperty.call(browserFeatures,o)&&(e+="&"+o+"="+browserFeatures[o]);t?e+="&data="+encodeWrapper(JSON2.stringify(t)):configCustomData&&(e+="&data="+encodeWrapper(JSON2.stringify(configCustomData)));var w=sortObjectByKeys(customVariablesPage),I=sortObjectByKeys(customVariablesEvent);if(e+=r(w,"cvar"),e+=r(I,"e_cvar"),customVariables){e+=r(customVariables,"_cvar");for(o in p)Object.prototype.hasOwnProperty.call(p,o)&&(""!==customVariables[o][0]&&""!==customVariables[o][1]||delete customVariables[o]);configStoreCustomVariablesInCookie&&setCookie(v,JSON2.stringify(customVariables),configSessionCookieTimeout,configCookiePath,configCookieDomain)}return configPerformanceTrackingEnabled&&(configPerformanceGenerationTime?e+="&gt_ms="+configPerformanceGenerationTime:performanceAlias&&performanceAlias.timing&&performanceAlias.timing.requestStart&&performanceAlias.timing.responseEnd&&(e+="&gt_ms="+(performanceAlias.timing.responseEnd-performanceAlias.timing.requestStart))),y.lastEcommerceOrderTs=isDefined(i)&&String(i).length?i:y.lastEcommerceOrderTs,setVisitorIdCookie(y),setSessionCookie(),e+=executePluginMethod(n),configAppendToTrackingUrl.length&&(e+="&"+configAppendToTrackingUrl),isFunction(configCustomRequestContentProcessing)&&(e=configCustomRequestContentProcessing(e)),e}

	 function logEcommerce(e,t,n,i,r,o){var a,s,c="idgoal=0",u=new Date,l=[];if(String(e).length&&(c+="&ec_id="+encodeWrapper(e),a=Math.round(u.getTime()/1e3)),c+="&revenue="+t,String(n).length&&(c+="&ec_st="+n),String(i).length&&(c+="&ec_tx="+i),String(r).length&&(c+="&ec_sh="+r),String(o).length&&(c+="&ec_dt="+o),ecommerceItems){for(s in ecommerceItems)Object.prototype.hasOwnProperty.call(ecommerceItems,s)&&(isDefined(ecommerceItems[s][1])||(ecommerceItems[s][1]=""),isDefined(ecommerceItems[s][2])||(ecommerceItems[s][2]=""),isDefined(ecommerceItems[s][3])&&0!==String(ecommerceItems[s][3]).length||(ecommerceItems[s][3]=0),isDefined(ecommerceItems[s][4])&&0!==String(ecommerceItems[s][4]).length||(ecommerceItems[s][4]=1),l.push(ecommerceItems[s]));c+="&ec_items="+encodeWrapper(JSON2.stringify(l))}c=getRequest(c,configCustomData,"ecommerce",a),sendRequest(c,configTrackerPause)}

	 function logEcommerceOrder(e,t,n,i,r,o){String(e).length&&isDefined(t)&&logEcommerce(e,t,n,i,r,o)}

	 function logEcommerceCartUpdate(e){isDefined(e)&&logEcommerce("",e,"","","","")}

	 function logPageView(e,t){var n=(new Date,getRequest("action_name="+encodeWrapper(titleFixup(e||configTitle)),t,"log"));sendRequest(n,configTrackerPause)}

	 function getClassesRegExp(e,t){var n,i="(^| )(piwik[_-]"+t;if(e)for(n=0;n<e.length;n++)i+="|"+e[n];return i+=")( |$)",new RegExp(i)}

	 function startsUrlWithTrackerUrl(e){return configTrackerUrl&&e&&0===String(e).indexOf(configTrackerUrl)}

	 function getLinkType(e,t,n,i){if(startsUrlWithTrackerUrl(t))return 0;var r=getClassesRegExp(configDownloadClasses,"download"),o=getClassesRegExp(configLinkClasses,"link"),a=new RegExp("\\.("+configDownloadExtensions.join("|")+")([?&#]|$)","i");return o.test(e)?"link":i||r.test(e)||a.test(t)?"download":n?0:"link"}

	 function getSourceElement(e){var t;for(t=e.parentNode;null!==t&&isDefined(t)&&!query.isLinkElement(e);)e=t,t=e.parentNode;return e}

	 function getLinkIfShouldBeProcessed(e){if(e=getSourceElement(e),query.hasNodeAttribute(e,"href")&&isDefined(e.href)){var t=query.getAttributeValueFromNode(e,"href");if(!startsUrlWithTrackerUrl(t)){var n=e.hostname||getHostName(e.href),i=n.toLowerCase(),r=e.href.replace(n,i),o=new RegExp("^(javascript|vbscript|jscript|mocha|livescript|ecmascript|mailto):","i");if(!o.test(r)){var a=getLinkType(e.className,r,isSiteHostName(i),query.hasNodeAttribute(e,"download"));if(a)return{type:a,href:r}}}}}

	 function buildContentInteractionRequest(e,t,n,i){var r=content.buildInteractionRequestParams(e,t,n,i);if(r)return getRequest(r,null,"contentInteraction")}

	 function buildContentInteractionTrackingRedirectUrl(e,t,n,i,r){if(isDefined(e)){if(startsUrlWithTrackerUrl(e))return e;var o=content.toAbsoluteUrl(e),a="redirecturl="+encodeWrapper(o)+"&";a+=buildContentInteractionRequest(t,n,i,r||e);var s="&";return configTrackerUrl.indexOf("?")<0&&(s="?"),configTrackerUrl+s+a}}

	 function isNodeAuthorizedToTriggerInteraction(e,t){if(!e||!t)return!1;var n=content.findTargetNode(e);return!content.shouldIgnoreInteraction(n)&&(n=content.findTargetNodeNoDefault(e),!(n&&!containsNodeElement(n,t)))}

	 function getContentInteractionToRequestIfPossible(e,t,n){if(e){var i=content.findParentContentNode(e);if(i&&isNodeAuthorizedToTriggerInteraction(i,e)){var r=content.buildContentBlock(i);if(r)return!r.target&&n&&(r.target=n),content.buildInteractionRequestParams(t,r.name,r.piece,r.target)}}}

	 function wasContentImpressionAlreadyTracked(e){if(!trackedContentImpressions||!trackedContentImpressions.length)return!1;var t,n;for(t=0;t<trackedContentImpressions.length;t++)if(n=trackedContentImpressions[t],n&&n.name===e.name&&n.piece===e.piece&&n.target===e.target)return!0;return!1}

	 function replaceHrefIfInternalLink(e){if(!e)return!1;var t=content.findTargetNode(e);if(!t||content.shouldIgnoreInteraction(t))return!1;var n=getLinkIfShouldBeProcessed(t);if(linkTrackingEnabled&&n&&n.type)return!1;if(query.isLinkElement(t)&&query.hasNodeAttributeWithValue(t,"href")){var i=String(query.getAttributeValueFromNode(t,"href"));if(0===i.indexOf("#"))return!1;if(startsUrlWithTrackerUrl(i))return!0;if(!content.isUrlToCurrentDomain(i))return!1;var r=content.buildContentBlock(e);if(!r)return;var o=r.name,a=r.piece,s=r.target;query.hasNodeAttributeWithValue(t,content.CONTENT_TARGET_ATTR)&&!t.wasContentTargetAttrReplaced||(t.wasContentTargetAttrReplaced=!0,s=content.toAbsoluteUrl(i),query.setAnyAttribute(t,content.CONTENT_TARGET_ATTR,s));var c=buildContentInteractionTrackingRedirectUrl(i,"click",o,a,s);return content.setHrefAttribute(t,c),!0}return!1}

	 function replaceHrefsIfInternalLink(e){if(e&&e.length){var t;for(t=0;t<e.length;t++)replaceHrefIfInternalLink(e[t])}}

	 function trackContentImpressionClickInteraction(e){return function(t){if(e){var n,i=content.findParentContentNode(e);if(t&&(n=t.target||t.srcElement),n||(n=e),isNodeAuthorizedToTriggerInteraction(i,n)){if(setExpireDateTime(configTrackerPause),query.isLinkElement(e)&&query.hasNodeAttributeWithValue(e,"href")&&query.hasNodeAttributeWithValue(e,content.CONTENT_TARGET_ATTR)){var r=query.getAttributeValueFromNode(e,"href");!startsUrlWithTrackerUrl(r)&&e.wasContentTargetAttrReplaced&&query.setAnyAttribute(e,content.CONTENT_TARGET_ATTR,"")}var o=getLinkIfShouldBeProcessed(e);if(linkTrackingInstalled&&o&&o.type)return o.type;if(replaceHrefIfInternalLink(i))return"href";var a=content.buildContentBlock(i);if(a){var s=a.name,c=a.piece,u=a.target,l=buildContentInteractionRequest("click",s,c,u);return sendRequest(l,configTrackerPause),l}}}}}

	 function setupInteractionsTracking(e){if(e&&e.length){var t,n;for(t=0;t<e.length;t++)n=content.findTargetNode(e[t]),n&&!n.contentInteractionTrackingSetupDone&&(n.contentInteractionTrackingSetupDone=!0,addEventListener(n,"click",trackContentImpressionClickInteraction(n)))}}

	 function buildContentImpressionsRequests(e,t){if(!e||!e.length)return[];var n,i;for(n=0;n<e.length;n++)wasContentImpressionAlreadyTracked(e[n])?(e.splice(n,1),n--):trackedContentImpressions.push(e[n]);if(!e||!e.length)return[];replaceHrefsIfInternalLink(t),setupInteractionsTracking(t);var r=[];for(n=0;n<e.length;n++)i=getRequest(content.buildImpressionRequestParams(e[n].name,e[n].piece,e[n].target),void 0,"contentImpressions"),r.push(i);return r}

	 function getContentImpressionsRequestsFromNodes(e){var t=content.collectContent(e);return buildContentImpressionsRequests(t,e)}

	 function getCurrentlyVisibleContentImpressionsRequestsIfNotTrackedYet(e){if(!e||!e.length)return[];var t;for(t=0;t<e.length;t++)content.isNodeVisible(e[t])||(e.splice(t,1),t--);return e&&e.length?getContentImpressionsRequestsFromNodes(e):[]}

	 function buildContentImpressionRequest(e,t,n){var i=content.buildImpressionRequestParams(e,t,n);return getRequest(i,null,"contentImpression")}

	 function buildContentInteractionRequestNode(e,t){if(e){var n=content.findParentContentNode(e),i=content.buildContentBlock(n);if(i)return t||(t="Unknown"),buildContentInteractionRequest(t,i.name,i.piece,i.target)}}

	 function buildEventRequest(e,t,n,i){return"e_c="+encodeWrapper(e)+"&e_a="+encodeWrapper(t)+(isDefined(n)?"&e_n="+encodeWrapper(n):"")+(isDefined(i)?"&e_v="+encodeWrapper(i):"")}

	 function logEvent(e,t,n,i,r){if(0===String(e).length||0===String(t).length)return!1;var o=getRequest(buildEventRequest(e,t,n,i),r,"event");sendRequest(o,configTrackerPause)}

	 function logSiteSearch(e,t,n,i){var r=getRequest("search="+encodeWrapper(e)+(t?"&search_cat="+encodeWrapper(t):"")+(isDefined(n)?"&search_count="+n:""),i,"sitesearch");sendRequest(r,configTrackerPause)}

	 function logGoal(e,t,n){var i=getRequest("idgoal="+e+(t?"&revenue="+t:""),n,"goal");sendRequest(i,configTrackerPause)}

	 function logLink(e,t,n,i,r){var o=t+"="+encodeWrapper(purify(e)),a=getContentInteractionToRequestIfPossible(r,"click",e);a&&(o+="&"+a);var s=getRequest(o,n,"link");sendRequest(s,i?0:configTrackerPause,i)}

	 function prefixPropertyName(e,t){return""!==e?e+t.charAt(0).toUpperCase()+t.slice(1):t}

	 function eventCovert(e){try{var t,n;window.event?(n=e.type,t=navigator.userAgent.indexOf("Firefox")>0?e.target:e.srcElement):(n=e.type,t=e.target),traceConvertEvent(t,n,t)}catch(i){}}

	 function traceConvertEvent(e,t,n,i){var r=monitorEvents.get(e.tagName);if(i=i?i:0,null==r)return void searchParentNodeTraceEvent(e,t,n,i);for(var o=0;o<r.length;o++){var a=r[o];if(""!=a.attrName&&""!=a.attrValue&&null!=e.attributes.getNamedItem(a.attrName)&&("class"==a.attrName.toLowerCase()&&hasClass(e,a.attrValue)||"class"!=a.attrName&&matchTarget(e.attributes.getNamedItem(a.attrName).nodeValue,a.attrValue)))return void logEvent("Jzl_convert",t,a.event_name,a.event_value)}return o==r.length?void searchParentNodeTraceEvent(e,t,n,i):void 0}

	 function hasClass(e,t){for(var n=e.attributes.getNamedItem("class").value,i=n.split(/\s+/),r=0;r<i.length;r++)if(i[r]==t)return!0;return!1}

	 function searchParentNodeTraceEvent(e,t,n,i){i++,i>MaxTierNum||(e=e.parentNode,e&&traceConvertEvent(e,t,n,i))}

	 function matchTarget(e,t){var n=e||"",i=t||"";try{if(i.indexOf("*")<0)return n==i;for(var r=i.split("*"),o=0;o<=r.length-1;o++)if(""!=r[o]&&!(n.indexOf(r[o])>=0))return!1;return!0}catch(a){return!1}}

	 function trackCallback(e){var t,n,i,r=["","webkit","ms","moz"];if(!configCountPreRendered)for(n=0;n<r.length;n++)if(i=r[n],Object.prototype.hasOwnProperty.call(documentAlias,prefixPropertyName(i,"hidden"))){"prerender"===documentAlias[prefixPropertyName(i,"visibilityState")]&&(t=!0);break}return t?void addEventListener(documentAlias,i+"visibilitychange",

	 	function o(){documentAlias.removeEventListener(i+"visibilitychange",o,!1),e()}):void e()}

	 function trackCallbackOnLoad(e){"complete"===documentAlias.readyState?e():windowAlias.addEventListener?windowAlias.addEventListener("load",e):windowAlias.attachEvent&&windowAlias.attachEvent("onLoad",e)}

	 function trackCallbackOnReady(e){var t=!1;t=documentAlias.attachEvent?"complete"===documentAlias.readyState:"loading"!==documentAlias.readyState,t?e():documentAlias.addEventListener?documentAlias.addEventListener("DOMContentLoaded",e):documentAlias.attachEvent&&documentAlias.attachEvent("onreadystatechange",e)}

	 function processClick(e){var t=getLinkIfShouldBeProcessed(e);t&&t.type&&(t.href=decodeWrapper(t.href),logLink(t.href,t.type,void 0,null,e))}

	 function isIE8orOlder(){return documentAlias.all&&!documentAlias.addEventListener}

	 function getKeyCodeFromEvent(e){var t=e.which,n=typeof e.button;return t||"undefined"===n||(isIE8orOlder()?1&e.button?t=1:2&e.button?t=3:4&e.button&&(t=2):0===e.button||"0"===e.button?t=1:1&e.button?t=2:2&e.button&&(t=3)),t}

	 function getNameOfClickedButton(e){switch(getKeyCodeFromEvent(e)){case 1:return"left";case 2:return"middle";case 3:return"right"}}

	 function getTargetElementFromEvent(e){return e.target||e.srcElement}function clickHandler(e){return function(t){t=t||windowAlias.event;var n=getNameOfClickedButton(t),i=getTargetElementFromEvent(t);if("click"===t.type){var r=!1;e&&"middle"===n&&(r=!0),i&&!r&&processClick(i)}else"mousedown"===t.type?"middle"===n&&i?(lastButton=n,lastTarget=i):lastButton=lastTarget=null:"mouseup"===t.type?(n===lastButton&&i===lastTarget&&processClick(i),lastButton=lastTarget=null):"contextmenu"===t.type&&processClick(i)}}

	 function addClickListener(e,t){addEventListener(e,"click",clickHandler(t),!1),t&&(addEventListener(e,"mouseup",clickHandler(t),!1),addEventListener(e,"mousedown",clickHandler(t),!1),
addEventListener(e,"contextmenu",clickHandler(t),!1))}

	function addClickListeners(e){if(!linkTrackingInstalled){linkTrackingInstalled=!0;var t,n=getClassesRegExp(configIgnoreClasses,"ignore"),i=documentAlias.links;if(i)for(t=0;t<i.length;t++)n.test(i[t].className)||addClickListener(i[t],e)}}

	function enableTrackOnlyVisibleContent(e,t,n){function i(){a=!0}if(isTrackOnlyVisibleContentEnabled)return!0;isTrackOnlyVisibleContentEnabled=!0;var r,o,a=!1;trackCallbackOnLoad(function(){function s(e){setTimeout(function(){isTrackOnlyVisibleContentEnabled&&(a=!1,n.trackVisibleContentImpressions(),s(e))},e)}

		function c(e){setTimeout(function(){isTrackOnlyVisibleContentEnabled&&(a&&(a=!1,n.trackVisibleContentImpressions()),c(e))},e)}if(e){for(r=["scroll","resize"],o=0;o<r.length;o++)documentAlias.addEventListener?documentAlias.addEventListener(r[o],i):windowAlias.attachEvent("on"+r[o],i);c(100)}t&&t>0&&(t=parseInt(t,10),s(t))})}

	function detectBrowserFeatures(){var e,t,n={pdf:"application/pdf",qt:"video/quicktime",realp:"audio/x-pn-realaudio-plugin",wma:"application/x-mplayer2",dir:"application/x-director",fla:"application/x-shockwave-flash",java:"application/x-java-vm",gears:"application/x-googlegears",ag:"application/x-silverlight"},i=windowAlias.devicePixelRatio||1;if(!new RegExp("MSIE").test(navigatorAlias.userAgent)){if(navigatorAlias.mimeTypes&&navigatorAlias.mimeTypes.length)for(e in n)Object.prototype.hasOwnProperty.call(n,e)&&(t=navigatorAlias.mimeTypes[n[e]],browserFeatures[e]=t&&t.enabledPlugin?"1":"0");"unknown"!=typeof navigator.javaEnabled&&isDefined(navigatorAlias.javaEnabled)&&navigatorAlias.javaEnabled()&&(browserFeatures.java="1"),isFunction(windowAlias.GearsFactory)&&(browserFeatures.gears="1"),browserFeatures.cookie=hasCookies()}browserFeatures.res=screenAlias.width*i+"x"+screenAlias.height*i}

	function registerHook(hookName,userHook){var hookObj=null;if(isString(hookName)&&!isDefined(registeredHooks[hookName])&&userHook){
	if(isObject(userHook))hookObj=userHook;
	else if(isString(userHook))
		try{eval("hookObj ="+userHook)}
		catch(ignore){}registeredHooks[hookName]=hookObj}
		return hookObj
	}

	var registeredHooks={},locationArray=urlFixup(documentAlias.domain,windowAlias.location.href,getReferrer()),domainAlias=domainFixup(locationArray[0]);
	try{var locationHrefAlias=decodeWrapper(locationArray[1])}catch(e){var locationHrefAlias=locationArray[1]}
	try{var configReferrerUrl=decodeWrapper(locationArray[2])}catch(e){var configReferrerUrl=locationArray[2]}

	var enableJSErrorTracking=!1,defaultRequestMethod="GET",configRequestMethod=defaultRequestMethod,defaultRequestContentType="application/x-www-form-urlencoded; charset=UTF-8",configRequestContentType=defaultRequestContentType,configTrackerUrl=trackerUrl||"",configApiUrl="",configAppendToTrackingUrl="",configTrackerSiteId=siteId||"",configUserId="",visitorUUID="",configCustomUrl,configTitle=documentAlias.title,configDownloadExtensions=["7z","aac","apk","arc","arj","asf","asx","avi","azw3","bin","csv","deb","dmg","doc","docx","epub","exe","flv","gif","gz","gzip","hqx","ibooks","jar","jpg","jpeg","js","mobi","mp2","mp3","mp4","mpg","mpeg","mov","movie","msi","msp","odb","odf","odg","ods","odt","ogg","ogv","pdf","phps","png","ppt","pptx","qt","qtm","ra","ram","rar","rpm","sea","sit","tar","tbz","tbz2","bz","bz2","tgz","torrent","txt","wav","wma","wmv","wpd","xls","xlsx","xml","z","zip"],configHostsAlias=[domainAlias],configIgnoreClasses=[],configDownloadClasses=[],configLinkClasses=[],configTrackerPause=500,configMinimumVisitTime,configHeartBeatDelay,heartBeatPingIfActivityAlias,configDiscardHashTag,configCustomData,
	configCampaignNameParameters=["pk_campaign","piwik_campaign","utm_campaign","utm_source","utm_medium"],configCampaignKeywordParameters=["pk_kwd","piwik_kwd","utm_term"],configCookieNamePrefix="_pk_",configCookieDomain,configCookiePath,configCookiesDisabled=!1,configDoNotTrack,configCountPreRendered,configConversionAttributionFirstReferrer,configVisitorCookieTimeout=339552e5,configSessionCookieTimeout=18e5,configReferralCookieTimeout=15768e6,configPerformanceTrackingEnabled=!0,configPerformanceGenerationTime=0,configStoreCustomVariablesInCookie=!1,customVariables=!1,configCustomRequestContentProcessing,customVariablesPage={},customVariablesEvent={},customVariableMaximumLength=200,ecommerceItems={},browserFeatures={},trackedContentImpressions=[],isTrackOnlyVisibleContentEnabled=!1,timeNextTrackingRequestCanBeExecutedImmediately=!1,linkTrackingInstalled=!1,linkTrackingEnabled=!1,heartBeatSetUp=!1,lastTrackerRequestTime=null,heartBeatTimeout,lastButton,lastTarget,hash=sha1,domainHash,monitorEvents=new EventMap,MaxTierNum=2;
	return heartBeatPingIfActivityAlias=function(){var e=new Date;if(lastTrackerRequestTime+configHeartBeatDelay<=e.getTime()){var t=getRequest("ping=1",null,"ping");return sendRequest(t,configTrackerPause),!0}return!1},
	detectBrowserFeatures(),updateDomainHash(),setVisitorIdCookie(),executePluginMethod("run",registerHook),{hook:registeredHooks,
	getHook:function(e){return registeredHooks[e]},
	getQuery:function(){return query},
	getContent:function(){return content},buildContentImpressionRequest:buildContentImpressionRequest,buildContentInteractionRequest:buildContentInteractionRequest,buildContentInteractionRequestNode:buildContentInteractionRequestNode,buildContentInteractionTrackingRedirectUrl:buildContentInteractionTrackingRedirectUrl,getContentImpressionsRequestsFromNodes:getContentImpressionsRequestsFromNodes,getCurrentlyVisibleContentImpressionsRequestsIfNotTrackedYet:getCurrentlyVisibleContentImpressionsRequestsIfNotTrackedYet,trackCallbackOnLoad:trackCallbackOnLoad,trackCallbackOnReady:trackCallbackOnReady,buildContentImpressionsRequests:buildContentImpressionsRequests,wasContentImpressionAlreadyTracked:wasContentImpressionAlreadyTracked,appendContentInteractionToRequestIfPossible:getContentInteractionToRequestIfPossible,setupInteractionsTracking:setupInteractionsTracking,trackContentImpressionClickInteraction:trackContentImpressionClickInteraction,internalIsNodeVisible:isVisible,isNodeAuthorizedToTriggerInteraction:isNodeAuthorizedToTriggerInteraction,replaceHrefIfInternalLink:replaceHrefIfInternalLink,
	getConfigDownloadExtensions:function(){return configDownloadExtensions},
	enableTrackOnlyVisibleContent:function(e,t){return enableTrackOnlyVisibleContent(e,t,this)},
	clearTrackedContentImpressions:function(){trackedContentImpressions=[]},
	getTrackedContentImpressions:function(){return trackedContentImpressions},
	clearEnableTrackOnlyVisibleContent:function(){isTrackOnlyVisibleContentEnabled=!1},
	disableLinkTracking:function(){linkTrackingInstalled=!1,linkTrackingEnabled=!1},
	getConfigVisitorCookieTimeout:function(){return configVisitorCookieTimeout},getRemainingVisitorCookieTimeout:getRemainingVisitorCookieTimeout,
	getVisitorId:function(){return getValuesFromVisitorIdCookie().uuid},
	getVisitorInfo:function(){return loadVisitorIdCookie()},
	getAttributionInfo:function(){return loadReferrerAttributionCookie()},
	getAttributionCampaignName:function(){return loadReferrerAttributionCookie()[0]},
	getAttributionCampaignKeyword:function(){return loadReferrerAttributionCookie()[1]},
	getAttributionReferrerTimestamp:function(){return loadReferrerAttributionCookie()[2]},
	getAttributionReferrerUrl:function(){return loadReferrerAttributionCookie()[3]},
	setTrackerUrl:function(e){configTrackerUrl=e},
	getTrackerUrl:function(){return configTrackerUrl},
	getSiteId:function(){return configTrackerSiteId},
	setSiteId:function(e){setSiteId(e)},
	setUserId:function(e){isDefined(e)&&e.length&&(configUserId=e,visitorUUID=hash(configUserId).substr(0,16))},
	getUserId:function(){return configUserId},
	setCustomData:function(e,t){isObject(e)?configCustomData=e:(configCustomData||(configCustomData={}),configCustomData[e]=t)},
	getCustomData:function(){return configCustomData},
	setCustomRequestProcessing:function(e){configCustomRequestContentProcessing=e},
	appendToTrackingUrl:function(e){configAppendToTrackingUrl=e},
	getRequest:function(e){return getRequest(e)},
	addPlugin:function(e,t){plugins[e]=t},
	setCustomVariable:function(e,t,n,i){var r;isDefined(i)||(i="visit"),isDefined(t)&&(isDefined(n)||(n=""),e>0&&(t=isString(t)?t:String(t),n=isString(n)?n:String(n),r=[t.slice(0,customVariableMaximumLength),n.slice(0,customVariableMaximumLength)],"visit"===i||2===i?(loadCustomVariables(),customVariables[e]=r):"page"===i||3===i?customVariablesPage[e]=r:"event"===i&&(customVariablesEvent[e]=r)))},
	getCustomVariable:function(e,t){var n;return isDefined(t)||(t="visit"),"page"===t||3===t?n=customVariablesPage[e]:"event"===t?n=customVariablesEvent[e]:"visit"!==t&&2!==t||(loadCustomVariables(),n=customVariables[e]),!(!isDefined(n)||n&&""===n[0])&&n},
	deleteCustomVariable:function(e,t){this.getCustomVariable(e,t)&&this.setCustomVariable(e,"","",t)},
	storeCustomVariablesInCookie:function(){configStoreCustomVariablesInCookie=!0},
	setLinkTrackingTimer:function(e){configTrackerPause=e},
	setDownloadExtensions:function(e){isString(e)&&(e=e.split("|")),configDownloadExtensions=e},
	addDownloadExtensions:function(e){var t;for(isString(e)&&(e=e.split("|")),t=0;t<e.length;t++)configDownloadExtensions.push(e[t])},
	removeDownloadExtensions:function(e){var t,n=[];for(isString(e)&&(e=e.split("|")),t=0;t<configDownloadExtensions.length;t++)indexOfArray(e,configDownloadExtensions[t])===-1&&n.push(configDownloadExtensions[t]);configDownloadExtensions=n},
	setDomains:function(e){configHostsAlias=isString(e)?[e]:e,configHostsAlias.push(domainAlias)},
	setIgnoreClasses:function(e){configIgnoreClasses=isString(e)?[e]:e},
	setRequestMethod:function(e){configRequestMethod=e||defaultRequestMethod},
	setRequestContentType:function(e){configRequestContentType=e||defaultRequestContentType},
	setReferrerUrl:function(e){configReferrerUrl=e},
	setCustomUrl:function(e){configCustomUrl=resolveRelativeReference(locationHrefAlias,e)},
	setDocumentTitle:function(e){configTitle=e},setAPIUrl:function(e){configApiUrl=e},
	setDownloadClasses:function(e){configDownloadClasses=isString(e)?[e]:e},
	setLinkClasses:function(e){configLinkClasses=isString(e)?[e]:e},
	setCampaignNameKey:function(e){configCampaignNameParameters=isString(e)?[e]:e},
	setCampaignKeywordKey:function(e){configCampaignKeywordParameters=isString(e)?[e]:e},
	discardHashTag:function(e){configDiscardHashTag=e},
	setCookieNamePrefix:function(e){configCookieNamePrefix=e,customVariables=getCustomVariablesFromCookie()},
	setCookieDomain:function(e){var t=domainFixup(e);isPossibleToSetCookieOnDomain(t)&&(configCookieDomain=t,updateDomainHash())},
	setCookiePath:function(e){configCookiePath=e,updateDomainHash()},
	setVisitorCookieTimeout:function(e){configVisitorCookieTimeout=1e3*e},
	setSessionCookieTimeout:function(e){configSessionCookieTimeout=1e3*e},
	setReferralCookieTimeout:function(e){configReferralCookieTimeout=1e3*e},
	setConversionAttributionFirstReferrer:function(e){configConversionAttributionFirstReferrer=e},
	disableCookies:function(){configCookiesDisabled=!0,browserFeatures.cookie="0",configTrackerSiteId&&deleteCookies()},
	deleteCookies:function(){deleteCookies()},
	setDoNotTrack:function(e){var t=navigatorAlias.doNotTrack||navigatorAlias.msDoNotTrack;configDoNotTrack=e&&("yes"===t||"1"===t),configDoNotTrack&&this.disableCookies()},
	addListener:function(e,t){addClickListener(e,t)},enableLinkTracking:function(e){linkTrackingEnabled=!0,hasLoaded?addClickListeners(e):registeredOnLoadHandlers.push(function(){addClickListeners(e)})},
	enableJSErrorTracking:function(){if(!enableJSErrorTracking){enableJSErrorTracking=!0;var e=windowAlias.onerror;windowAlias.onerror=function(t,n,i,r,o){return trackCallback(function(){var e="JavaScript Errors",o=n+":"+i;r&&(o+=":"+r),logEvent(e,o,t)}),!!e&&e(t,n,i,r,o)}}},
	disablePerformanceTracking:function(){configPerformanceTrackingEnabled=!1},setGenerationTimeMs:function(e){configPerformanceGenerationTime=parseInt(e,10)},enableHeartBeatTimer:function(e){e=Math.max(e,1),configHeartBeatDelay=1e3*(e||15),null!==lastTrackerRequestTime&&setUpHeartBeat()},
	disableHeartBeatTimer:function(){heartBeatDown(),configHeartBeatDelay=null,window.removeEventListener("focus",heartBeatOnFocus),window.removeEventListener("blur",heartBeatOnBlur)},killFrame:function(){windowAlias.location!==windowAlias.top.location&&(windowAlias.top.location=windowAlias.location)},redirectFile:function(e){"file:"===windowAlias.location.protocol&&(windowAlias.location=e)},
	setCountPreRendered:function(e){configCountPreRendered=e},trackGoal:function(e,t,n){trackCallback(function(){logGoal(e,t,n)})},trackLink:function(e,t,n,i){trackCallback(function(){logLink(e,t,n,i)})},trackPageView:function(e,t){trackedContentImpressions=[],trackCallback(isOverlaySession(configTrackerSiteId)?function(){injectOverlayScripts(configTrackerUrl,configApiUrl,configTrackerSiteId)}:function(){logPageView(e,t)})},
	trackAllContentImpressions:function(){isOverlaySession(configTrackerSiteId)||trackCallback(function(){trackCallbackOnReady(function(){var e=content.findContentNodes(),t=getContentImpressionsRequestsFromNodes(e);sendBulkRequest(t,configTrackerPause)})})},trackVisibleContentImpressions:function(e,t){isOverlaySession(configTrackerSiteId)||(isDefined(e)||(e=!0),isDefined(t)||(t=750),enableTrackOnlyVisibleContent(e,t,this),trackCallback(function(){trackCallbackOnLoad(function(){var e=content.findContentNodes(),t=getCurrentlyVisibleContentImpressionsRequestsIfNotTrackedYet(e);sendBulkRequest(t,configTrackerPause)})}))},trackContentImpression:function(e,t,n){isOverlaySession(configTrackerSiteId)||e&&(t=t||"Unknown",trackCallback(function(){var i=buildContentImpressionRequest(e,t,n);sendRequest(i,configTrackerPause)}))},trackContentImpressionsWithinNode:function(e){!isOverlaySession(configTrackerSiteId)&&e&&trackCallback(function(){isTrackOnlyVisibleContentEnabled?trackCallbackOnLoad(function(){var t=content.findContentNodesWithinNode(e),n=getCurrentlyVisibleContentImpressionsRequestsIfNotTrackedYet(t);sendBulkRequest(n,configTrackerPause)}):trackCallbackOnReady(function(){var t=content.findContentNodesWithinNode(e),n=getContentImpressionsRequestsFromNodes(t);sendBulkRequest(n,configTrackerPause)})})},
	trackContentInteraction:function(e,t,n,i){isOverlaySession(configTrackerSiteId)||e&&t&&(n=n||"Unknown",trackCallback(function(){var r=buildContentInteractionRequest(e,t,n,i);sendRequest(r,configTrackerPause)}))},trackContentInteractionNode:function(e,t){!isOverlaySession(configTrackerSiteId)&&e&&trackCallback(function(){var n=buildContentInteractionRequestNode(e,t);sendRequest(n,configTrackerPause)})},trackEvent:function(e,t,n,i,r){trackCallback(function(){logEvent(e,t,n,i,r)})},
	setMaxTierNum:function(e){MaxTierNum=e},trackAllClickEvent:function(){addEventListener(window,"click",eventCovert,!1)},trackDomClickEvent:function(e,t,n,i,r,o,a,s,c){function u(e){if(void 0!==e.attrName&&"id"==e.attrName.toLowerCase()&&document.getElementById(e.attrValue))return void addEventListener(document.getElementById(e.attrValue),"click",l(e));if(void 0!==e.attrName&&"class"==e.attrName.toLowerCase()&&document.getElementsByClassName(e.attrValue).length>0)for(var t=document.getElementsByClassName(e.attrValue),n=0,i=t.length;n<i;n++)addEventListener(t[n],"click",l(e));else setTimeout(function(){u(e)},1e3)}function l(e){return function(){var t="";if("class"==e.collect_type){var n=document.getElementsByClassName(e.collect_type_value);if(n.length>0)for(var i=0,r=n.length;i<r;i++)t+=n[i].value}else if("name"==e.collect_type){var n=document.getElementsByName(e.collect_type_value);if(n.length>0)for(var i=0,r=n.length;i<r;i++)t+=n[i].value}if(t&&"\u8bf7\u8f93\u5165\u60a8\u7684\u7535\u8bdd\u53f7\u7801"!=t){var o={};o[e.collect_value_key]=t,logEvent("Jzl_convert","click",e.event_name,e.event_value,o)}}}var f={};f.tagType=e,f.attrName=t,f.attrValue=n,f.event_type=i,f.event_name=r,f.event_value=o,f.collect_type=a,f.collect_type_value=s,f.collect_value_key=c,u(f)},
	trackDomBySelectorClickEvent:function(e,t,n,i,r,o,a,s){function c(e){if(void 0!==e.selector&&"single"===e.selectorType&&document.querySelector(e.selector))return void addEventListener(document.querySelector(e.selector),"click",u(e));if(void 0!==e.selector&&"list"===e.selectorType&&document.querySelectorAll(e.selector).length>0)for(var t=document.querySelectorAll(e.selector),n=0,i=t.length;n<i;n++)addEventListener(t[n],"click",u(e));else setTimeout(function(){c(e)},1e3)}function u(e){return function(){var t="";if("class"==e.collect_type){var n=document.getElementsByClassName(e.collect_type_value);if(n.length>0)for(var i=0,r=n.length;i<r;i++)t+=n[i].value}else if("name"==e.collect_type){var n=document.getElementsByName(e.collect_type_value);if(n.length>0)for(var i=0,r=n.length;i<r;i++)t+=n[i].value}if(""===t)logEvent("Jzl_convert",e.event_type,e.event_name,e.event_value);else if(t&&"\u8bf7\u8f93\u5165\u60a8\u7684\u7535\u8bdd\u53f7\u7801"!=t){var o={};o[e.collect_value_key]=t,logEvent("Jzl_convert","click",e.event_name,e.event_value,o)}}}var l={};l.selector=e,l.selectorType=t,l.event_type=n,l.event_name=i,l.event_value=r,l.collect_type=o,l.collect_type_value=a,l.collect_value_key=s,c(l)},monitorEvent:function(e,t,n,i,r,o){var a={};a.tagType=e,a.attrName=t,a.attrValue=n,a.event_type=i,a.event_name=r,a.event_value=o,monitorEvents.append(new String(e).toUpperCase(),a)},
	trackSiteSearch:function(e,t,n){trackCallback(function(){logSiteSearch(e,t,n)})},setEcommerceView:function(e,t,n,i){isDefined(n)&&n.length?n instanceof Array&&(n=JSON2.stringify(n)):n="",customVariablesPage[5]=["_pkc",n],isDefined(i)&&String(i).length&&(customVariablesPage[2]=["_pkp",i]),(isDefined(e)&&e.length||isDefined(t)&&t.length)&&(isDefined(e)&&e.length&&(customVariablesPage[3]=["_pks",e]),isDefined(t)&&t.length||(t=""),customVariablesPage[4]=["_pkn",t])},addEcommerceItem:function(e,t,n,i,r){e.length&&(ecommerceItems[e]=[e,t,n,i,r])},trackEcommerceOrder:function(e,t,n,i,r,o){logEcommerceOrder(e,t,n,i,r,o)},
	trackEcommerceCartUpdate:function(e){logEcommerceCartUpdate(e)}}}function TrackerProxy(){return{push:apply}}function applyMethodsInOrder(e,t){var n,i,r={};for(n=0;n<t.length;n++){var o=t[n];for(r[o]=1,i=0;i<e.length;i++)if(e[i]&&e[i][0]){var a=e[i][0];o===a&&(apply(e[i]),delete e[i],r[a]>1&&void 0!==console&&console&&console.error&&console.error("The method "+a+' is registered more than once in "paq" variable. Only the last call has an effect. Please have a look at the multiple Piwik trackers documentation: http://developer.piwik.org/guides/tracking-javascript-guide#multiple-piwik-trackers'),r[a]++)}}return e}var expireDateTime,plugins={},documentAlias=document,navigatorAlias=navigator,screenAlias=screen,windowAlias=window,performanceAlias=windowAlias.performance||windowAlias.mozPerformance||windowAlias.msPerformance||windowAlias.webkitPerformance,hasLoaded=!1,registeredOnLoadHandlers=[],encodeWrapper=windowAlias.encodeURIComponent,decodeWrapper=windowAlias.decodeURIComponent,urldecode=unescape,asyncTracker,iterator,JZLPiwik,query={htmlCollectionToArray:function(e){var t,n=[];if(!e||!e.length)return n;for(t=0;t<e.length;t++)n.push(e[t]);return n},find:function(e){if(!document.querySelectorAll||!e)return[];var t=document.querySelectorAll(e);return this.htmlCollectionToArray(t)},findMultiple:function(e){if(!e||!e.length)return[];var t,n,i=[];for(t=0;t<e.length;t++)n=this.find(e[t]),i=i.concat(n);return i=this.makeNodesUnique(i)},
	findNodesByTagName:function(e,t){if(!e||!t||!e.getElementsByTagName)return[];var n=e.getElementsByTagName(t);return this.htmlCollectionToArray(n)},makeNodesUnique:function(e){var t=[].concat(e);if(e.sort(function(e,n){if(e===n)return 0;var i=indexOfArray(t,e),r=indexOfArray(t,n);return i===r?0:i>r?-1:1}),e.length<=1)return e;var n,i=0,r=0,o=[];for(n=e[i++];n;)n===e[i]&&(r=o.push(i)),n=e[i++]||null;for(;r--;)e.splice(o[r],1);return e},getAttributeValueFromNode:function(e,t){if(this.hasNodeAttribute(e,t)){if(e&&e.getAttribute)return e.getAttribute(t);if(e&&e.attributes){var n=typeof e.attributes[t];if("undefined"!==n){if(e.attributes[t].value)return e.attributes[t].value;if(e.attributes[t].nodeValue)return e.attributes[t].nodeValue;var i,r=e.attributes;if(r){for(i=0;i<r.length;i++)if(r[i].nodeName===t)return r[i].nodeValue;return null}}}}},hasNodeAttributeWithValue:function(e,t){var n=this.getAttributeValueFromNode(e,t);return!!n},hasNodeAttribute:function(e,t){if(e&&e.hasAttribute)return e.hasAttribute(t);if(e&&e.attributes){var n=typeof e.attributes[t];return"undefined"!==n}return!1},hasNodeCssClass:function(e,t){if(e&&t&&e.className){var n="string"==typeof e.className?e.className.split(" "):[];if(-1!==indexOfArray(n,t))return!0}return!1},findNodesHavingAttribute:function(e,t,n){if(n||(n=[]),!e||!t)return n;var i=getChildrenFromNode(e);if(!i||!i.length)return n;var r,o;for(r=0;r<i.length;r++)o=i[r],this.hasNodeAttribute(o,t)&&n.push(o),n=this.findNodesHavingAttribute(o,t,n);return n},
	findFirstNodeHavingAttribute:function(e,t){if(e&&t){if(this.hasNodeAttribute(e,t))return e;var n=this.findNodesHavingAttribute(e,t);return n&&n.length?n[0]:void 0}},findFirstNodeHavingAttributeWithValue:function(e,t){if(e&&t){if(this.hasNodeAttributeWithValue(e,t))return e;var n=this.findNodesHavingAttribute(e,t);if(n&&n.length){var i;for(i=0;i<n.length;i++)if(this.getAttributeValueFromNode(n[i],t))return n[i]}}},findNodesHavingCssClass:function(e,t,n){if(n||(n=[]),!e||!t)return n;if(e.getElementsByClassName){var i=e.getElementsByClassName(t);return this.htmlCollectionToArray(i)}var r=getChildrenFromNode(e);if(!r||!r.length)return[];var o,a;for(o=0;o<r.length;o++)a=r[o],this.hasNodeCssClass(a,t)&&n.push(a),n=this.findNodesHavingCssClass(a,t,n);return n},findFirstNodeHavingClass:function(e,t){if(e&&t){if(this.hasNodeCssClass(e,t))return e;var n=this.findNodesHavingCssClass(e,t);return n&&n.length?n[0]:void 0}},isLinkElement:function(e){if(!e)return!1;var t=String(e.nodeName).toLowerCase(),n=["a","area"],i=indexOfArray(n,t);return i!==-1},setAnyAttribute:function(e,t,n){e&&t&&(e.setAttribute?e.setAttribute(t,n):e[t]=n)}},content={CONTENT_ATTR:"data-track-content",CONTENT_CLASS:"piwikTrackContent",CONTENT_NAME_ATTR:"data-content-name",CONTENT_PIECE_ATTR:"data-content-piece",CONTENT_PIECE_CLASS:"piwikContentPiece",CONTENT_TARGET_ATTR:"data-content-target",CONTENT_TARGET_CLASS:"piwikContentTarget",CONTENT_IGNOREINTERACTION_ATTR:"data-content-ignoreinteraction",CONTENT_IGNOREINTERACTION_CLASS:"piwikContentIgnoreInteraction",location:void 0,
	findContentNodes:function(){var e="."+this.CONTENT_CLASS,t="["+this.CONTENT_ATTR+"]",n=query.findMultiple([e,t]);return n},findContentNodesWithinNode:function(e){if(!e)return[];var t=query.findNodesHavingCssClass(e,this.CONTENT_CLASS),n=query.findNodesHavingAttribute(e,this.CONTENT_ATTR);if(n&&n.length){var i;for(i=0;i<n.length;i++)t.push(n[i])}return query.hasNodeAttribute(e,this.CONTENT_ATTR)?t.push(e):query.hasNodeCssClass(e,this.CONTENT_CLASS)&&t.push(e),t=query.makeNodesUnique(t)},findParentContentNode:function(e){if(e)for(var t=e,n=0;t&&t!==documentAlias&&t.parentNode;){if(query.hasNodeAttribute(t,this.CONTENT_ATTR))return t;if(query.hasNodeCssClass(t,this.CONTENT_CLASS))return t;if(t=t.parentNode,n>1e3)break;n++}},findPieceNode:function(e){var t;return t=query.findFirstNodeHavingAttribute(e,this.CONTENT_PIECE_ATTR),t||(t=query.findFirstNodeHavingClass(e,this.CONTENT_PIECE_CLASS)),t?t:e},findTargetNodeNoDefault:function(e){if(e){var t=query.findFirstNodeHavingAttributeWithValue(e,this.CONTENT_TARGET_ATTR);return t?t:(t=query.findFirstNodeHavingAttribute(e,this.CONTENT_TARGET_ATTR))?t:(t=query.findFirstNodeHavingClass(e,this.CONTENT_TARGET_CLASS),t?t:void 0)}},findTargetNode:function(e){var t=this.findTargetNodeNoDefault(e);return t?t:e},findContentName:function(e){if(e){var t=query.findFirstNodeHavingAttributeWithValue(e,this.CONTENT_NAME_ATTR);if(t)return query.getAttributeValueFromNode(t,this.CONTENT_NAME_ATTR);var n=this.findContentPiece(e);if(n)return this.removeDomainIfIsInLink(n);if(query.hasNodeAttributeWithValue(e,"title"))return query.getAttributeValueFromNode(e,"title");var i=this.findPieceNode(e);if(query.hasNodeAttributeWithValue(i,"title"))return query.getAttributeValueFromNode(i,"title");var r=this.findTargetNode(e);return query.hasNodeAttributeWithValue(r,"title")?query.getAttributeValueFromNode(r,"title"):void 0}},
	findContentPiece:function(e){if(e){var t=query.findFirstNodeHavingAttributeWithValue(e,this.CONTENT_PIECE_ATTR);if(t)return query.getAttributeValueFromNode(t,this.CONTENT_PIECE_ATTR);var n=this.findPieceNode(e),i=this.findMediaUrlInNode(n);return i?this.toAbsoluteUrl(i):void 0}},findContentTarget:function(e){if(e){var t=this.findTargetNode(e);if(query.hasNodeAttributeWithValue(t,this.CONTENT_TARGET_ATTR))return query.getAttributeValueFromNode(t,this.CONTENT_TARGET_ATTR);var n;if(query.hasNodeAttributeWithValue(t,"href"))return n=query.getAttributeValueFromNode(t,"href"),this.toAbsoluteUrl(n);var i=this.findPieceNode(e);return query.hasNodeAttributeWithValue(i,"href")?(n=query.getAttributeValueFromNode(i,"href"),this.toAbsoluteUrl(n)):void 0}},isSameDomain:function(e){if(!e||!e.indexOf)return!1;if(0===e.indexOf(this.getLocation().origin))return!0;var t=e.indexOf(this.getLocation().host);return 8>=t&&0<=t},removeDomainIfIsInLink:function(e){var t="^https?://[^/]+",n="^.*//[^/]+";return e&&e.search&&-1!==e.search(new RegExp(t))&&this.isSameDomain(e)&&(e=e.replace(new RegExp(n),""),e||(e="/")),e},findMediaUrlInNode:function(e){if(e){var t=["img","embed","video","audio"],n=e.nodeName.toLowerCase();if(-1!==indexOfArray(t,n)&&query.findFirstNodeHavingAttributeWithValue(e,"src")){var i=query.findFirstNodeHavingAttributeWithValue(e,"src");return query.getAttributeValueFromNode(i,"src")}if("object"===n&&query.hasNodeAttributeWithValue(e,"data"))return query.getAttributeValueFromNode(e,"data");if("object"===n){var r=query.findNodesByTagName(e,"param");if(r&&r.length){var o;for(o=0;o<r.length;o++)if("movie"===query.getAttributeValueFromNode(r[o],"name")&&query.hasNodeAttributeWithValue(r[o],"value"))return query.getAttributeValueFromNode(r[o],"value")}var a=query.findNodesByTagName(e,"embed");if(a&&a.length)return this.findMediaUrlInNode(a[0])}}},trim:function(e){return e&&String(e)===e?e.replace(/^\s+|\s+$/g,""):e},
	isOrWasNodeInViewport:function(e){if(!e||!e.getBoundingClientRect||1!==e.nodeType)return!0;var t=e.getBoundingClientRect(),n=documentAlias.documentElement||{},i=t.top<0;i&&e.offsetTop&&(i=e.offsetTop+t.height>0);var r=n.clientWidth;windowAlias.innerWidth&&r>windowAlias.innerWidth&&(r=windowAlias.innerWidth);var o=n.clientHeight;return windowAlias.innerHeight&&o>windowAlias.innerHeight&&(o=windowAlias.innerHeight),(t.bottom>0||i)&&t.right>0&&t.left<r&&(t.top<o||i)},isNodeVisible:function(e){var t=isVisible(e),n=this.isOrWasNodeInViewport(e);return t&&n},buildInteractionRequestParams:function(e,t,n,i){var r="";return e&&(r+="c_i="+encodeWrapper(e)),t&&(r&&(r+="&"),r+="c_n="+encodeWrapper(t)),n&&(r&&(r+="&"),r+="c_p="+encodeWrapper(n)),i&&(r&&(r+="&"),r+="c_t="+encodeWrapper(i)),r},buildImpressionRequestParams:function(e,t,n){var i="c_n="+encodeWrapper(e)+"&c_p="+encodeWrapper(t);return n&&(i+="&c_t="+encodeWrapper(n)),i},buildContentBlock:function(e){if(e){var t=this.findContentName(e),n=this.findContentPiece(e),i=this.findContentTarget(e);return t=this.trim(t),n=this.trim(n),i=this.trim(i),{name:t||"Unknown",piece:n||"Unknown",target:i||""}}},collectContent:function(e){if(!e||!e.length)return[];var t,n,i=[];for(t=0;t<e.length;t++)n=this.buildContentBlock(e[t]),isDefined(n)&&i.push(n);return i},setLocation:function(e){this.location=e},getLocation:function(){var e=this.location||windowAlias.location;return e.origin||(e.origin=e.protocol+"//"+e.hostname+(e.port?":"+e.port:"")),e},
	toAbsoluteUrl:function(e){if((!e||String(e)!==e)&&""!==e)return e;if(""===e)return this.getLocation().href;if(e.search(/^\/\//)!==-1)return this.getLocation().protocol+e;if(e.search(/:\/\//)!==-1)return e;if(0===e.indexOf("#"))return this.getLocation().origin+this.getLocation().pathname+e;if(0===e.indexOf("?"))return this.getLocation().origin+this.getLocation().pathname+e;if(0===e.search("^[a-zA-Z]{2,11}:"))return e;if(e.search(/^\//)!==-1)return this.getLocation().origin+e;var t="(.*/)",n=this.getLocation().origin+this.getLocation().pathname.match(new RegExp(t))[0];return n+e},isUrlToCurrentDomain:function(e){var t=this.toAbsoluteUrl(e);if(!t)return!1;var n=this.getLocation().origin;return n===t||0===String(t).indexOf(n)&&":"!==String(t).substr(n.length,1)},setHrefAttribute:function(e,t){e&&t&&query.setAnyAttribute(e,"href",t)},shouldIgnoreInteraction:function(e){var t=query.hasNodeAttribute(e,this.CONTENT_IGNOREINTERACTION_ATTR),n=query.hasNodeCssClass(e,this.CONTENT_IGNOREINTERACTION_CLASS);return t||n}};addEventListener(windowAlias,"beforeunload",beforeUnloadHandler,!1),addReadyListener(),Date.prototype.getTimeAlias=Date.prototype.getTime,asyncTracker=new Tracker;

	var applyFirst=["disableCookies","setTrackerUrl","setAPIUrl","setCookiePath","setCookieDomain","setUserId","setSiteId","enableLinkTracking"];

	for(_jzlpaq=applyMethodsInOrder(_jzlpaq,applyFirst),iterator=0;iterator<_jzlpaq.length;iterator++)_jzlpaq[iterator]&&apply(_jzlpaq[iterator]);return _jzlpaq=new TrackerProxy,JZLPiwik={addPlugin:function(e,t){plugins[e]=t},getTracker:function(e,t){return isDefined(t)||(t=this.getAsyncTracker().getSiteId()),isDefined(e)||(e=this.getAsyncTracker().getTrackerUrl()),new Tracker(e,t)},
	getAsyncTracker:function(){return asyncTracker}},"function"==typeof define&&define.amd&&define("jzlpiwik",[],function(){return JZLPiwik}),JZLPiwik}()),window&&window.piwikAsyncInit&&window.piwikAsyncInit(),function(){var e=typeof AnalyticsTracker;"undefined"===e&&(AnalyticsTracker=JZLPiwik)}(),"function"!=typeof jzl_piwik_log&&(jzl_piwik_log=function(documentTitle,siteId,piwikUrl,customData){"use strict";function getOption(optionName){try{return eval("piwik_"+optionName)}catch(ignore){}}var option,piwikTracker=JZLPiwik.getTracker(piwikUrl,siteId);piwikTracker.setDocumentTitle(documentTitle),piwikTracker.setCustomData(customData),option=getOption("tracker_pause"),option&&piwikTracker.setLinkTrackingTimer(option),option=getOption("download_extensions"),option&&piwikTracker.setDownloadExtensions(option),option=getOption("hosts_alias"),option&&piwikTracker.setDomains(option),option=getOption("ignore_classes"),option&&piwikTracker.setIgnoreClasses(option),piwikTracker.trackPageView(),getOption("install_tracker")&&(piwik_track=function(e,t,n,i){piwikTracker.setSiteId(t),piwikTracker.setTrackerUrl(n),piwikTracker.trackLink(e,i)},piwikTracker.enableLinkTracking())});






