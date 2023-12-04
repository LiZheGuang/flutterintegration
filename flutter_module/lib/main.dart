import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:image_picker/image_picker.dart';

import 'detail.dart' as Detail;

void main() => runApp(const MyApp());

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      routes: {
        '/': (context) => const MyHomePage(title: '演示DEMO'),
        '/details': (context) => const Detail.WebViewExample(),
      },
      initialRoute: "/",
      title: 'Flutter Demo',
      theme: ThemeData(primarySwatch: Colors.blue),
      // home: const MyHomePage(title: '演示 MethodChannel'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = MethodChannel('com.bqt.test/base_channel');
  String _batteryLevel = '';
  int _counter = 0;

  // 用于互相主动发送消息，也可用于传递字符串或半结构化的消息
  static const BasicMessageChannel<String> _channel =
      BasicMessageChannel('com.bqt.test/basic_channel', StringCodec());
  String _message = "message";
  String _response = "response";
  void initState() {
    super.initState();
    debugPrint("initState");
    _channel.setMessageHandler((String? message) async {
      debugPrint("message: $message"); // 收到的 native 发送的消息
      _message = message ?? "null";
      setState(() => _message);
      return '已收到【$message】'; // 对 native 的响应
    });
  }

  void _request() async {
    final String? response =
        await _channel.send('来自 Dart 的请求'); // 对 native 发起请求
    debugPrint("response: $response"); // 收到的 native 响应
    _response = response ?? "null";
    setState(() => _response);
  }
  // 用于互相主动发送消息，也可用于传递字符串或半结构化的消息

  void _incrementCounter() {
    setState(() =>
        _counter++); // This call to setState causes rerun the build method below
    _getBatteryLevel().then((value) {
      setState(() => _batteryLevel = value);
    });
  }

  void _clickCallback() async {
    var response = await platform.invokeMethod('onCallGetFunction');
    debugPrint("安卓原生方法");

    debugPrint(response);
  }

  void _clickOnCallNeXtSwitchBackground() async {
    var response = await platform.invokeMethod('onCallNeXtSwitchBackground');
    debugPrint("安卓原生方法");
    debugPrint(response);
  }

  void _clickFunction(methodName) async {
    var response = await platform.invokeMethod(methodName);
    debugPrint("安卓原生方法");
    debugPrint(response);
  }

  void _clickOpenPhoto() async {
    final ImagePicker picker = ImagePicker();
    // Pick an image.
    final XFile? image = await picker.pickImage(source: ImageSource.gallery);
    // Capture a photo.
    final XFile? photo = await picker.pickImage(source: ImageSource.camera);
    // Pick a video.
    final XFile? galleryVideo =
        await picker.pickVideo(source: ImageSource.gallery);
    // Capture a video.
    final XFile? cameraVideo =
        await picker.pickVideo(source: ImageSource.camera);
    // Pick multiple images.
    final List<XFile> images = await picker.pickMultiImage();
    // Pick singe image or video.
    final XFile? media = await picker.pickMedia();
    // Pick multiple images and videos.
    final List<XFile> medias = await picker.pickMultipleMedia();
  }

  Future<String> _getBatteryLevel() async {
    try {
      final int result = await platform.invokeMethod('getBatteryLevel');
      return '电量 $result % .}';
    } on PlatformException catch (e) {
      return "获取失败: '${e.message}'.";
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(title: const Text('这是标题')),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.start,
            children: [
              Text('电量 $_batteryLevel'),
              Text('点击次数 $_counter'),
              ElevatedButton(
                onPressed: () {
                  _incrementCounter();
                  // 在这里编写按钮点击时要执行的代码
                },
                child: const Text('电池'),
              ),
              ElevatedButton(
                onPressed: () {
                  _clickCallback();
                },
                child: const Text('功能'),
              ),
              ElevatedButton(
                onPressed: () {
                  _clickOnCallNeXtSwitchBackground();
                },
                child: const Text('切换APP到后台'),
              ),
              ElevatedButton(
                onPressed: () {
                  _clickFunction('onCallNextAddRouter');
                },
                child: const Text('跳转到Android页面'),
              ),
              ElevatedButton(
                onPressed: () {
                  Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context) => const Detail.WebViewExample()),
                  );
                },
                child: const Text('跳转到Flutter页面'),
              ),
              ElevatedButton(
                onPressed: () {
                  _clickOpenPhoto();
                },
                child: const Text('打开相机'),
              ),
              ElevatedButton(
                onPressed: () {
                  _request();
                },
                child: Text(_response),
              ),
              Text(_message),
            ],
          ),
        ),
        // floatingActionButton:FloatingActionButton(onPressed: _incrementCounter),
      ),
    );
  }
}
