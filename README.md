### QRtool 是一个生成二维码和解析二维码工具， [github地址](https://github.com/kecson/JavaProject/tree/master/qrcodetool) <br/>
####[下载jar包](https://github.com/kecson/Qrcode/releases/download/1.2/QRtool-1.2.jar)
命令参数：<br/>
 * -h       显示此帮助信息 <br/>
 * -e       生成二维码 <br/>
 * -d       解析二维码 <br/>
 *********************************************** <br/>
 <pre>-e    生成二维码相关参数:   <br/>   
    -text=    二维码内容   <br/>       
    -file=    保存的文件名，如：qrcode.jpg  <br/>        
    -dir=     保存的文件目录，如：D:/ <br/>       
    -logo=    二维码中间Logo的图片路径(此项可不填)<br/></pre>
 --------------------------------------------------------- <br/>
 生成二维码命令格式为:<br/>
 不带Logo命令:   `java -jar QRtool-1.0.jar -e -text=hello1 -file=qr.jpg -dir=C:/` <br/>
 带有Logo命令:   `java -jar QRtool-1.0.jar -e -text=hello1 -file=qr.jpg -dir=C:/ -logo=C:/logo.jpg` <br/>
 *********************************************************<br/>
  <pre>-d  解析二维码参数 :   -f 二维码文件路径 <br/></pre>
 --------------------------------------------------------- <br/>
 解析二维码命令格式为: <br/>
 `java -jar QRtool-1.0.jar -d  D:/qr-logo.jpg` <br/>
