function formattrendseries(a){function b(a,b){void 0===C[a].forecastdaytimes&&b&&b.day_start&&b.day_end&&b.week_start&&b.week_end&&(C[a].forecastdaytimes=[b.day_start,b.day_end],C[a].forecastweektimes=[b.week_start.split("/")[0],b.week_end.split("/")[1]]),void 0===C[a].forecastIndexes&&(C[a].forecastIndexes=[],C[a].forecastWeekIndexes=[]),void 0===b?(C[a].forecastIndexes.push(null),C[a].forecastWeekIndexes.push(null)):b.str_day&&b.str_week&&(C[a].forecastIndexes.push(b.str_day.split(",")),C[a].forecastWeekIndexes.push(b.str_week.split(",")))}var c=G.Time,d=G.Utils,e=a||{},f=e.all,g=e.forecast||{},h=e.news,i=e.pc,j=e.wise,k=c.checkTimeLength(c.fixedtime(f[0].period))+1;if(h&&h[0]&&h[0].userIndexes.split(",").length>k)for(var l=0,m=h.length;m>l;l++){var n=h[l].userIndexes.split(",");n.length=k,h[l].userIndexes=n.join(",")}for(var o=["整体趋势","PC趋势","移动趋势的"],p=[],l=0,m=f.length;m>l;l++)p.push(f[l].key);
var q,r,s=function(a){return void 0===a||""===a?[]:a.split(",")},t=function(a){if(void 0===a)return null;for(var b=[],c=0,d=a.length;d>c;c++)b.push(s(a[c].userIndexes));return b},
u=function(a){if(void 0===a)return null;for(var b=0,c=a.length,d=0;c>b;b++)d+=Math.floor(a[b]);return Math.floor(d/c)},
v=G._getWeekIndexes=function(a){if(void 0===a)return null;for(var b=arguments[1],d=[],e=c.fixedtime(a[0].period),f=e[0],g=(e[1],c.getDay(f).k),h=0,i=a.length;i>h;h++){for(var j=s(a[h].userIndexes),k=g,l=f,m=[],n=[],o=0,p=j.length;p>o;o++)m.push(j[o]),6===k||o===p-1?(b?n.push({t1:l,t2:c.getDate(f,o),v:u(m)}):n.push(u(m)),k=0,m=[]):(0===k&&(l=c.getDate(f,o)),k++);d.push(n)}return d},w=t(f),x=t(i),y=t(j),z=v(f),A=v(i),B=v(j);h&&(q=t(h),r=v(h));var C={all:{},pc:{},wise:{}};if(g)for(var l=0,m=p.length;m>l;l++){var D=p[l],E=g[D];E&&(b("all",E.all),b("pc",E.pc),b("wise",E.wise))}
var F=function(a,b,d){for(var e=(c.daysBetween(b[0],b[1]),c.daysBetween(b[0],d[0])),f=c.daysBetween(b[0],d[1]),g=[],h=0,i=a.length;i>h;h++)g.push(a[h].slice(e,f+1));return g},
H=function(a,b,d){for(var e=(c.getDay(b[0]).k,c.getYearWeek(b[0],d[0])),f=c.getYearWeek(b[0],d[1]),g=[],h=0,i=a.length;i>h;h++)g.push(a[h].slice(e,f+1));return g},I={day:{},week:{}},
J=function(a,b,d,e){for(var f=c.daysBetween(e[0],d[0]),g=c.daysBetween(e[0],d[1]),h=0,i=a.length;i>h;h++)if(a[h]&&b[h]){var j=a[h].slice(g+1),k=a[h].slice(0,f);a[h]=k.concat(b[h],j)}},
K=function(a,b,d,e){for(var f=c.getYearWeek(e[0],d[0]),g=c.getYearWeek(e[0],d[1]),h=0,i=a.length;i>h;h++)if(a[h]&&b[h]){var j=a[h].slice(g+1),k=a[h].slice(0,f);a[h]=k.concat(b[h],j)}},
L=function(a,b){var d=b.cur,e=b.usertimes,f=b.datatimes,g=c.daysBetween(e[0],e[1])+1<=365?"day":"week";"day"===g?"all"===d?J(w,a,e,f):"pc"===d?J(x,a,e,f):"wise"===d&&J(y,a,e,f):"week"===g&&("all"===d?K(z,a,e,f):"pc"===d?K(A,a,e,f):"wise"===d&&K(B,a,e,f))};return{tabs:o,legend:p,getData:function(a,b){if(e.hasOwnProperty(a)){var f,g=e[a],h=c.fixedtime(g[0].period),i=h[0],j=h[1];if(void 0===b)var k=c.getDate(j,-29),b=[k,j];else if(d.isString(b)){var l=b;b="all"!==l?[c.getDate(h[1],-l+1),h[1]]:h}var m,k=b[0],n=b[1];k>n&&(m=k,k=n,n=m),i>k&&(k=i),n>j&&(n=j),b=[k,n];var o,s,t,u,v=C[a],D=function(){c.daysBetween(k,n)+1<=365?(f="day","all"===a?(o=F(w,h,b),q&&(s=F(q,h,b))):"pc"===a?o=F(x,h,b):"wise"===a&&(o=F(y,h,b)),v&&v.forecastdaytimes&&v.forecastIndexes&&(t=v.forecastdaytimes,u=v.forecastIndexes)):(f="week","all"===a?(o=H(z,h,b),r&&(s=H(r,h,b))):"pc"===a?o=H(A,h,b):"wise"===a&&(o=H(B,h,b)),v&&v.forecastweektimes&&v.forecastWeekIndexes&&(t=v.forecastweektimes,u=v.forecastWeekIndexes))};D();
var E=function(a){var b,d=[];D(),b="day"===f?c.daysBetween(k,a):a;for(var e=0,g=o.length;g>e;e++)d.push([p[e],o[e][b]]);return d};if(I[f][a])u=I[f][a];else if(u){if(n>t[1])u=null;else{var J;J="day"===f?c.daysBetween(k,t[1])+1:c.getYearWeek(k,t[1])+1;for(var K=o[0].length,M=d.getMaxLength(u),N=J-K,O=0,P=u.length;P>O;O++){var Q=u[O],R=o[O][K-1];if(Q){if(k<=t[0])for(var S=0,T=N-M;T>S;S++)Q.unshift(R);else k>t[0]&&(Q=Q.slice(M-N));Q.unshift(R)}u[O]=Q}t[0]=n}I[f][a]=u}
var U=function(a,b){var d=b.cur,e=b.usertimes,f=(b.datatimes,c.daysBetween(e[0],e[1])+1<=365?"day":"week");u=a,I[f][d]=a};return{type:a,usertimes:[k,n],datatimes:h,showtype:f,userIndexes:o,mediaIndexes:s,getDatazoomIndexes:function(a){switch(a){case"all":return z[0];case"pc":return A[0];case"wise":return B[0];default:return z[0]}},forecaststate:function(a,b){return C[a]&&void 0!==C[a].forecastIndexes&&void 0!==C[a].forecastdaytimes?b:"disable"},forecasttimes:t,forecastuserIndexes:u,setIndexes:L,setForecastData:U,getuserdata:E}}G.debug("没有分类的数据")}}}

function getWeekAvgs(a,b){b=b||0;for(var c=[],d=0,e=0,f=0,g=a.length;g>f;f++)d+=+a[f],e+=1,++b%7===0&&(c.push(d/e),d=e=0);return e&&c.push(d/e),c}function time2Pointime(a,b){var c=G.Time.daysBetween(a,"1970-01-04");return"week"==b?Math.floor(c/7):c}

function point2Weektime(a){var b=7*a*864e5+G.Time.getParse("1970-01-04");return[b,b+6048e5-1]}

//在html里被调用了:注意这个函数调了很多其他的函数
function trendChartInit(a){
	function b(a){var b=125,c=T("#trendYimg");
	if(a&&a.max_y){
		var d=PPval.dataface+"IndexShow/getYaxis/?res="+PPval.ppt+"&res2="+PPval.res2+"&max_y="+a.max_y+"&min_y="+a.min_y+"&axis="+a.step_y;
		c.attr("src")!==d&&c.attr("src",d).show()
	}
	c.css({left:T("#trend").width()-b})}
	function c(a,b,c,d,e){
		if(!b||!c||b>c)
			return null;
		var f=c-b+1,g=Math.abs(d-b);
		if(a=a.slice(0,f),f=a.length,d>=b+f)
			return null;
		var h=[e];
		b>d?h.length=g:a=a.slice(g+1);
		for(var i=h.length;i--;)
			h[i]=e;
		return h.concat(a)
	}
	function d(a,b,d,e,f){
		var g=[],h=0;
		BID.each(BID.getParams().word,
			function(i,j){
				var k=(a[j]||{})[e]||{},l=k["str_"+b+"_100"]||"",m=k[b+"_start"],n=k[b+"_end"],o=1;
				if(!l.length||!m||!n)
					return void g.push(null);  //Fixme:???????
				l=l.split(","),"week"==b&&(m=m.split("/")[0],n=n.split("/")[0],o=7);
				var p=c(l,time2Pointime(m,b),time2Pointime(n,b),time2Pointime(f,b),(d[i]||[0]).slice(-1)[0]);
				g.push(p),p&&(h=Math.max(h,p.length*o))
			}
		);
		var i=[f,BID.fnsDate(f).date(h-1).toString()];
		return h||(i[1]=i[0]),{values:g,times:i}
	}
	function e(a){var b=a.cur,c=a.state,d=BID.getParams(),e="",f={type:d.type,startdate:a.usertimes[0],enddate:a.usertimes[1],forecast:a[b].forecast===!0?1:0};if(a.usertimes[1]!==a.datatimes[1]&&(f.forecast=0),e=T.url.jsonToQuery(f),1==d.type){for(var g=[],h=d.area,i=0;i<h.length;i++)"T"===c[i]&&g.push(h[i]);f.area=g,e+="&area="+g.join(",")}else{for(var g=[],j=d.word,i=0;i<j.length;i++)"T"===c[i]&&g.push(j[i]);f.word=g,e+="&word="+encodeURIComponent(g.join(","))}return[e,f]}
	function f(a,b,c){
		a=a||window;
		var d=200,e="",f={},g='<tr>            <td class="view-dot"><span style="background:#{color}"></span></td>            <td class="view-label">#{word}：</td><td class="view-value">#{value}</td>            </tr>',
		j=function(b){
			b=b||[];
			for(var c=a.getParams.C32().tags,d='<table id="trendPopTab" class="view-table" style="font-family:simsun;">',e=0;e<c.length;e++)
				d+=T.string(g).format({color:a.getColor(e),word:BID.subStr(c[e]),value:b[e]||""});
			return d+"</table>"
		};
		return a.evts.care("trend_viewbox_pop",function(c,d){
			a.dataInterface("IndexShow/show/",
				T.url.jsonToQuery({classType:1,"res3[]":c,className:"view-value"}),
				function(a){
					var c=(a.data||{}).code;
					return c&&0==a.status?(d&&(f[b.cur][d]=j(c)),void(d===e&&T("#trendPopTab .view-value").each(function(a){var b=c[a]||"";b=b.replace("<style","<br /><style"),this.innerHTML=b}))):void location.reload()},{noCache:!0})},{gt:d}),function(d,g,k){if(!i||i!==k){i=k;var l="day",m=String(k),n=b.usertimes[0];if(m.indexOf("-")<0&&(l="week",m=""+(time2Pointime(n,"week")+ +k)),m===e)return void this.viewbox(d,g);e=m;var o=f[b.cur]||(f[b.cur]={}),p=o[m];if(!p){p=j();var q=[],r=c[b.cur],s="userIndexes_enc",t=k;"day"===l&&(s="userIndexes_enc",t=a.fnsDate(m).difs(n,"d"));for(var u=r.length;u--;)"string"==typeof r[u][s]&&(r[u][s]=r[u][s].split(",")),q[u]=r[u][s][t];a.evts("trend_viewbox_pop",null,q,m)}var v={limit:4,content:p};if("day"===l)v.title=m+" "+G.Time.getDay(m).v;else{var w=point2Weektime(m),x=h[b.cur];x&&(w[0]=Math.max(w[0],x[0]),w[1]=Math.min(w[1],x[1])),v.title=a.fnsDate(w[0]).toString()+" ~ "+a.fnsDate(w[1]).toString()+" 周平均值"}this.viewbox(d,g,v)}}}function g(a,b,c,d){function e(){var a=(b.barVals||[])[i],c=T("#trendBarVal"),d=(c.find(".contentWord").html(a||"&nbsp;&nbsp;"),26);return a&&(d=c.show().width()),c.css({top:j.y,left:j.x+j.w/2-d/2}).show(),a}
	function f(){T("#trendBarVal").hide()}
	function h(a){
		BID.dataInterface("IndexShow/show/",T.url.jsonToQuery({classType:1,"res3[]":a,className:"barAvgVal"}),
			function(a){
				k=0;
				var c=(b.barVals=(a.data||{}).code||[])[0]||"",f=c.match(/<style>(.*?)<\/style>/)||[],g=T("#innerStyle4BarAvg")[0];
				f[1]&&(g.styleSheet?g.styleSheet.cssText=f[1]:g.innerHTML=f[1]);
				for(var h=d.state.length;h--;)
					"T"!==d.state[h]&&(b.barVals[h]="0");e()
			},{noCache:!0}
		)
	}
	a=a||window;
	var i,j,k;
	return g.viewPop=e,g.hidePop=f,
		function(a,b,f){
			j=f,i=b;
			var g=e();
			if(!g&&!k){
				k=1;
				for(var l=[],m=c[d.cur],n=m.length;n--;)
					l[n]=m[n].userIndexes_avg_enc||"";
				h(l)
			}
		return""
		}
}

	var h={},i=null,j=BID.sentimBlock("trend-wrap").title([]).tabTils([]),k=G.Time.fixedtime(BID.getParams.time()[0]),l={};
	BID.dataInterface("Search/getAllIndex/",
		"startdate="+k[0]+"&enddate="+k[1],
		function(c){function i(b){for(var c=["all","pc","wise"],d=(a.cur,0),e=c.length;e>d;d++){var f=b[c[d]];if(!a.isarea){for(var g=[],i=BID.getParams().word,j=i.length;j--;)for(var k=f.length;k--;)if(f[k].key===i[j]){g[j]=f[k];break}f=b[c[d]]=g}if(f&&f.length>0){if(f[0].period){var l=f[0].period.split("|");l[0]=+BID.fnsDate(l[0]),l[1]=+BID.fnsDate(l[1]),h[c[d]]=l}for(var k=0,m=f.length;m>k;k++){var n=f[k]||{};n.userIndexes_30&&(n.userIndexes=n.userIndexes_30)}}}return b}function k(a){for(var b=["all","pc","wise"],c=0,d=b.length;d>c;c++){for(var e=a[b[c]],f=[],g=BID.getParams().area,h=g.length;h--;)for(var i=e.length;i--;)if(e[i].area==g[h]){f[h]=e[i];break}if(e=a[b[c]]=f,e&&e.length>0)for(var i=0,j=e.length;j>i;i++){var k=e[i]||{};k.key=k.areaName}}return a}function m(a,b){for(var c=[],d=(a||[]).length,e=0;d>e;e++){var f=a[e].userIndexes_100;f?(f=f.split(","),f.length>365&&(f=getWeekAvgs(f,G.Time.getDay(b).k)),c.push(f)):c.push([])}return c}function n(a){for(var b in I[a]){var c=T("#trend-"+b),d=I[a][b];"disable"===d?c.css("display","none"):(c.css("display","block"),d===!0?c.addClass("select"):d===!1&&c.removeClass("select"))}}function o(c,f){var h=I.cur,i=e(I),j=i[0];i=i[1],g.hidePop(),delete l.barVals,BID.dataInterface("Search/getSubIndex/",j,function(c){if(!c.data||0!=c.status)return void T("#trend").html('<div style="padding-top:120px;text-align:center;"><p class="nodata">暂无相关数据</p></div>');c.data=function(b){for(var c=["all","pc","wise"],d=0,e=c.length;e>d;d++){var f=b[c[d]];if(a.isarea){for(var g=[],h=BID.getParams().area,i=h.length;i--;)for(var j=f.length;j--;)if(f[j].area==h[i]){g[i]=f[j];break}f=b[c[d]]=g}else{for(var g=[],h=BID.getParams().word,i=h.length;i--;)for(var j=f.length;j--;)if(f[j].key===h[i]){g[i]=f[j];break}f=b[c[d]]=g}if(f&&f.length>0)for(var j=0,k=f.length;k>j;j++){var l=f[j]||{};l.userIndexes_100&&(l.userIndexes=l.userIndexes_100)}}return b}(c.data);var e=c.data[h],g=I.usertimes,j=G.Time.daysBetween(g[0],g[1])+1<=365?"day":"week",k=[];if(b(e[0]),BID.each(e,function(a,b){var c=r[h][a],d=b.userIndexes_100;c.userIndexes_enc=b.userIndexes_enc,c.userIndexes_avg_enc=b.userIndexes_avg_enc,d=d&&d.length?d.split(","):[],k.push(d)}),"toggle"===f.type?(f.value[2]=f.value[2]||{},f.value[2]["series.userIndexes"]=k):void 0===f.value.series?f.value["series.userIndexes"]=k:f.value.series.userIndexes=k,i.forecast&&c.data.forecast)var l=d(c.data.forecast,j,k,I.cur,g[1]);else l={values:[],times:[g[1],g[1]]};"toggle"===f.type?(f.value[2]=f.value[2]||{},f.value[2]["series.forecasttimes"]=l.times,f.value[2]["series.forecastuserIndexes"]=l.values):void 0===f.value.series?(f.value["series.forecasttimes"]=l.times,f.value["series.forecastuserIndexes"]=l.values):(f.value.series.forecasttimes=l.times,f.value.series.forecastuserIndexes=l.values),M.refresh(["trend"],f),window.pageStatus=!0},{loading:T("#trend").parent()[0]})}function p(a){I.cur=a;var b=460,c=430;"all"!==a&&(b=400,c=370),n(a),x.setSize(x.width,b),T("#trend").css({height:b+20+"px"});var d=y.getData(a,I.chartselect||[B,C]);I.usertimes=d.usertimes,j.title(["",d.usertimes.join(" 至 ")]),o(["trend"],{type:"data",value:{series:d,"opts.control":I[a]}});var e=D.getDatazoomIndexes(I.cur),f=d.datatimes,g=d.usertimes;M.refresh(["datazoom"],{type:"data",value:{y:c,"opts.series":e,datatimes:f,usertimes:g}})}function q(a,b){"disable"!==I.all[a]&&(I.all[a]=b),"disable"!==I.pc[a]&&(I.pc[a]=b),"disable"!==I.wise[a]&&(I.wise[a]=b)}if(BID.evts("trendProfile_data",null,c),0!==c.status)return void T("#trend").html('<div style="padding-top:120px;text-align:center;"><p class="nodata">暂无相关数据</p></div>');var r=c.data||{},s=T("#trend").width();(a.isarea||"0"!=BID.getParams().area[0]||!r.is_forecast)&&(a.forecast="disable"),BID.getParams().time[0].length>2&&(a.forecast="disable");var t=0;"pc"===a.cur?t=1:"wise"===a.cur&&(t=2),r=i(r),a.isarea&&(r=k(r)),b(r[a.cur][0]);var u=G.Time,v=BID.getParams.time(),w=u.fixedtime(v[0]);""!==a.usertime1&&""!==a.usertime2&&(w=[a.usertime1,a.usertime2]);var x=G.canvas("trend",s,"all"===a.cur?460:400),y=formattrendseries(r),z=y.legend,A=w,B=A[0],C=A[1],D=y.getData(a.cur,A),E=D.datatimes,F=D.forecaststate(a.cur,!1);j.title(["",A.join(" 至 ")]);var H=m(r[a.cur],B);H.length>0&&(D.userIndexes=H),""!==a.forecast&&(F=a.forecast),0==r.is_forecast&&(F="disable"),("disable"===F||"disable"===a.forecast)&&T("#trend-forecast").css("display","none");var I={cur:a.cur,chartselect:null,datatimes:E,usertimes:A,state:["T","T","T","T","T"].slice(0,D.userIndexes.length),all:{topline:!0,meanline:!1,forecast:F},pc:{topline:"disable",meanline:!1,forecast:F},wise:{topline:"disable",meanline:!1,forecast:F}};""!==a.topline,""!==a.meanline;var J;G.ui.tab.addType("trendtype",function(a){p(a.value)});var K=function(a){function b(a,b,c){h.push({stat:b,searches:a,i:c,index:i++})}for(var c,d,e,f=0,g=a.length,h=[],i=0;g>f;f++){if(d=parseInt(a[f]||0,10),!(g-1>f)){b(d,"down"===c?"bottom":"top",f);break}e=parseInt(a[f+1]||0,10),0!==f?e>d?("down"===c&&b(d,"bottom",f),c="up"):d>e&&("up"===c&&b(d,"top",f),c="down"):d>e?(b(d,"top",f),c="down"):(b(d,"bottom",f),c="up")}return h},L=function(a){var b=[];return _.each(a,function(a){for(var c,d=K(a),e=0,f=d.length,g=[];f>e;e++)if(c=d[e],"top"===c.stat){var h=d[c.index-1]||d[c.index+1],i=h.searches||c.searches;c.k=parseInt(1e3*c.searches/i,10),g.push(c)}g.sort(function(a,b){return b.k-a.k}),g.length>10&&(g.length=10),b.push(g)}),b},M=x.chart("list",[["legend",20,100,x.width-20,30,z,{textlimit:12,fn:function(a,b){b?(I.state[a]="F",o(["trend"],{type:"toggle",value:[a,"off"]})):(I.state[a]="T",o(["trend"],{type:"toggle",value:[a,"on"]}))}}],["trendchart",20,130,x.width-20,290,D,{typeid:"trend",control:I[I.cur],USER_N:7,userlimitvalues:[99,0],useindexesdata:!0,getuserdata:!1,hidevalues:!0,viewfn:f(BID,I,r),line_f_fn:function(a,b){},formatfn:g(BID,l,r,I),gettopline_fn:function(a,b,d,e,f,g,h,i,j,k){function l(a,c){var j=u.daysBetween(g[0],g[1]),k=u.daysBetween(g[0],a),l=k*e/j+b,m="week"===i.showtype?u.getYearWeek(g[0],a):u.daysBetween(g[0],a),n=i.userIndexes[c][m],o=d+f-f*(n-h[1])/(h[0]-h[1]);return{x:l,y:o}}var m=G.chart.CONFIG,n=j||a.set();G.Utils.clearpaperset(n),J=g[0].replace(/-/g,"")+"|"+g[1].replace(/-/g,"");var o=(c.data||[],i.userIndexes),g=i.usertimes,p=L(o),q=G.Utils.getMaxLength(o),r=u.daysBetween(g[0],g[1]),s={};_.each(p,function(a,b){var c=[];_.each(a,function(a){var b=a.i*r/(q-1),d=u.getDate(g[0],b);c.push(d.replace(/-/g,""))}),s[encodeURIComponent(z[b])]=c});var t=function(b,c,d){var e=b.date;e=e.slice(0,4)+"-"+e.slice(4,6)+"-"+e.slice(6);var f=g[0],h=g[1];if(e>=f&&h>=e){var i=l(e,c),j=i.x,k=i.y,o=a.rect(j-6,k-6-12,13,13).attr({fill:m.COLORS[c],stroke:"none",opacity:0}),p=a.path("M"+j+","+k+"L"+j+","+(k-5)).attr({stroke:m.COLORS[c],fill:"none"}),q=a.text(j,k-12,m.TOPLINETEXT[d]).attr({fill:m.COLORS[c]}),r=a.rect(j-6,k-6-12,13,13).attr({fill:"#fff",cursor:"pointer",stroke:"none",opacity:0});n.push(o,p,q,r),function(a,b,c,d,e){r.hover(function(){c[2].attr({fill:"#fff"}),c[0].attr({opacity:1})},function(){c[2].attr({fill:m.COLORS[a]}),c[0].attr({opacity:0})}),r.click(function(b){T("#newsbox")&&T("#newsbox").hide();var c=G.Utils.getPointerPosition(b),f=$(document).width();

	BID.dataInterface("News/getTopNews/",
		"wd="+z[a]+"&dt="+d.replace(/-/g,""),
		function(a){
			var b="";
			if(a&&a.data)
				for(var g=0,h=a.data.length;h>g;g++){
					var i=a.data[g];
					i.title&&(b+='<a target="_blank" href="'+i.url+'">'+i.title+"</a>")
				}
				if(""===b){
					var j=e.split("	"),
					i={title_url:j[1],title:j[0]};
					i.title_url&&i.title&&(b='<a target="_blank" href="'+i.title_url+'">'+i.title+"</a>")
				}
				x.chart("viewbox",200,100,
					{title:d,content:""===b?"暂时没有数据":b},
					{id:"newsbox",close:!0}
				),$("#newsbox").css({left:c.x>f/2?c.x-300:c.x,top:c.y}).show()
		}
	)
})}(c,d,[o,p,q,r],e,b.detail)}};  //Fixme:????????
	
	BID.dataInterface("Newwordgraph/getNewsByDateList/",
		$.param({wordlist:s}),
		function(a){
			if(a&&a.data){
				var b=a.data;
				_.each(b,function(a,b){
					_.each(a.res,function(a,c){t(a,b,c)})
				})
			}
		}
	)
}}],["datazoom",20,"all"===I.cur?430:370,x.width-40,30,E,A,{typeid:"datazoom",series:D.getDatazoomIndexes("all"),fn:function(a){I.usertimes=a,j.title(["",a.join(" 至 ")]),o(["trend"],{type:"data",value:{series:y.getData(I.cur,a)}})}}]]);T("#trend_copyright").show(),T(".chartselect").click(function(){var a=this.rel;T(this).addClass("chartselect-click").siblings().removeClass("chartselect-click"),I.chartselect=T(this).attr("rel");var b,c,d=y.getData(I.cur).datatimes;"all"!==a?(c=d[1],b=u.getDate(c,-a+1)):(b=d[0],c=d[1]),I.usertimes=[b,c],o(["trend"],{type:"data",value:{series:y.getData(I.cur,[b,c])}}),M.refresh(["datazoom"],{type:"data",value:{datatimes:d,usertimes:[b,c]}}),j.title(["",b+" 至 "+c])});var N=BID.getParams.C32().c32,O=N[0],P=$("a.chartselect");switch(O){case"最近7天":P.eq(0).addClass("chartselect-click");break;case"最近30天":P.eq(1).addClass("chartselect-click")}T("#trend-control label").click(function(a){if(7!=T.browser.ie||"LABEL"!=a.target.tagName){var b=T(this).attr("id"),c=b.replace("trend-","");T(this).toggleClass("select");var d=I.cur,e=T(this).hasClass("select");if(q(c,e),"meanline"===c&&T("#trend_copyright").css({left:e?"130px":"20px"}),"forecast"===c){var f=y.getData(d,I.chartselect||[B,C]);I.usertimes=f.usertimes.slice(),M.refresh(["datazoom"],{type:"data",value:{datatimes:f.datatimes,usertimes:f.usertimes}}),o(["trend"],{type:"data",value:{series:f,"opts.control":I[d]}})}else o(["trend"],{type:"data",value:{"opts.control":I[d]}})}}),BID.evts.care("width_resize",function(){var a,c=T("#trend").width();a="all"!==I.cur?400:460,x.setSize(c,a),o(["trend"],{type:"data",value:{w:c-20}}),M.refresh(["datazoom"],{type:"data",value:{w:x.width-40}}),b()},{gt:500}),a.show_share&&T("#trend-wrap .titlBar .ttools").show().click(function(){var b={tpl:"trendKey",cur:I.cur,usertime1:I.usertimes[0],usertime2:I.usertimes[1],topline:I[I.cur].topline,meanline:I[I.cur].meanline,forecast:I[I.cur].forecast};b.topline="disable",b.meanline="disable",a.isarea&&(b.isarea="true"),BID.shareBlockImg(b)}),window.pageStatus=!0},{loading:T("#trend").parent()[0]})}

!function(a){a=a||window;
var b="tProfile",c=a[b]={};
//enc2viwCode
c.enc2viwCode=function(b,c){
	a.dataInterface("IndexShow/show/",
		T.url.jsonToQuery({classType:2,"res3[]":b,className:c.className}),
		function(a){var b=(a.data||{}).code;b&&0==a.status&&c.funCbk(b)})},

c.setimgVal=function(a,b){for(var c=a.find(".enc2imgVal"),d=c.length;d--;)c.eq(d).html(b[d])},
c.c2r_wm=function(b,c,d){var e=[],f=[];return a.each(b.all,function(a,g){var h={word:g[c]},i=b.wise[a].ratio;h.all_qoq=g.ratio["qoq_"+d],h.all_yoy=g.ratio["yoy_"+d],h.wise_qoq=i["qoq_"+d],h.wise_yoy=i["yoy_"+d],e.push(h),f.push(g.ratio["agv_"+d+"_enc"],i["agv_"+d+"_enc"])}),{items:e,encArr:f}},
c.setData=function(b,d,e){var f=d.children(".tabCont"),g=b.data||{};g.all=g.all||{},g.pc=g.pc||{},g.wise=g.wise||{};var h="0"==a.getParams().type?"key":"areaName",i=c.c2r_wm(g,h,"w");f.eq(0).html(T.template(e,{items:i.items,tbClass:"profWagv"})),

a.tProfile.enc2viwCode(i.encArr,
	{className:"profWagv",
	funCbk:function(a){
		c.setimgVal(f.eq(0),a)
	}
	}
),
i=c.c2r_wm(g,h,"m"),f.eq(1).html(T.template(e,{items:i.items,tbClass:"profMagv"})),
a.tProfile.enc2viwCode(i.encArr,
	{className:"profMagv",
	funCbk:function(a){c.setimgVal(f.eq(1),a)}})},

c.init=function(b){var c=a.fnsDate,
	d=c(PPval.dataStm).toString(),
	e=a.getParams.timeDay,f=[c(d).date(-e[12]).toString()+" 至 "+d,c(d).date(-e[13]).toString()+" 至 "+d],g=a.getParams().type,h=[" ",f[0]];"0"!=g&&(h=["",f[0]," "]);var i=a.sentimBlock(b).title(h).tabTils(["近7天","近30天"]),j=i.getEl("tabConts"),k=(j.children(".tabCont"),i.getEl("baiduTpl")[0].id);i.getEl("tabTitle").show(),a.toLoading(j[0]),a.evts.care(b+"_data",function(b){if(a.tProfile.setData(b,j,k),T.browser.ie<=8){var c=i.getEl("tabUl").find(".tabLi");c.eq(1).click(),c.eq(0).click()}a.toLoading(j[0],1)}).care(i.getEl("tabUl")[0].id+"_tabClick",function(a){var b=[" ",f[a]];"0"!=g&&(b=["",f[a]," "]),i.title(b)})}}(BID);