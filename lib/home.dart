import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class Home extends StatefulWidget {
  const Home({Key? key}) : super(key: key);

  @override
  State<Home> createState() => _HomeState();
}

class _HomeState extends State<Home> {

  MethodChannel platform=MethodChannel('backgroundservice');
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar:AppBar(),
      body: Container(
        height: MediaQuery.of(context).size.height,
        width: MediaQuery.of(context).size.width,
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text('Here'),
            ElevatedButton(onPressed: (){
              startService();
            },child: Text('Press'),)
          ],
        ),
      ),

    );
  }
  void startService() async {
    dynamic value=await platform.invokeMethod('startservice');
    print(value);


  }
}
