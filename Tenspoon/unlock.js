import React, {Component} from 'react';
import {
  StyleSheet,
  PanResponder,
  View,
  Text,
  NativeModules
} from 'react-native';

const CIRCLE_SIZE   = 40;
const CIRCLE_COLOR  = 'blue';
const CIRCLE_HIGHLIGHT_COLOR = 'green';
const distoryAdview = () => {
	NativeModules.NativeMenu.distoryAdview();
};

const adAction = () => {
	NativeModules.NativeMenu.adAction();
};


class Unlock extends Component {
  //초기값 지정
  _panResponder = {}
  _previousLeft = 0
  _previousTop  = 0
  _circleStyles = {}
  circle = null

  _debugTanA = 0


  constructor(props) {
    super(props);
    this.state = {
      numberActiveTouches: 0,
      moveX: 0,
      moveY: 0,
      x0: 0,
      y0: 0,
      dx: 0,
      dy: 0,
      vx: 0,
      vy: 0
    };
  }


  componentWillMount() {
    this._panResponder = PanResponder.create({
      onStartShouldSetPanResponder: this._handleStartShouldSetPanResponder,
      onMoveShouldSetPanResponder: this._handleMoveShouldSetPanResponder,
      onPanResponderGrant: this._handlePanResponderGrant,
      onPanResponderMove: this._handlePanResponderMove,
      onPanResponderRelease:this._handlePanResponderEnd,
      onPanResponderTerminate:this._handlePanResponderEnd,
    });

    this._previousLeft  = -20;
    this._previousTop   = 100;
    this._circleStyles  = {
      style: {
        left: this._previousLeft,
        top: this._previousTop,
      }
    };
  }

  componentDidMount() {
    this._updatePosition();
  }

  render(){
    return(
      <View style={styles.container}>

        <View
          ref={ (circle) => {
              this.circle = circle;
            }
          }
          style={styles.circle} {...this._panResponder.panHandlers}/>


          <Text style={styles.debug}> {this.state.numberActiveTouches} touches, dx: {this.state.dx}  {"\n"}
            moveX: {this.state.moveX}  {"\n"}
            moveY: {this.state.moveY}   {"\n"}
            previousTop: {this._previousTop}  {"\n"}
            styleTop: {this._circleStyles.style.top} {"\n"}
            tanA: {this._debugTanA} {"\n"}
          </Text>


      </View>
    );
  }


  //_highlight와 _unhighlight는 PanResponder 메서드에 의해 호출되어
  //사용자에게 시각적 피드백을 주게 된다

  _highlight = () => {
    this.circle && this.circle.setNativeProps({
      style: {
        backgroundColor: CIRCLE_HIGHLIGHT_COLOR
      }
    });
  }

  _unHighlight = () => {
    this.circle && this.circle.setNativeProps({
      style: {
        backgroundColor: CIRCLE_COLOR
      }
    });
  }

  // 원의 위치는 setNativeProps 함수를 통해 변경한다.
  _updatePosition = () => {
    this.circle && this.circle.setNativeProps(this._circleStyles);
  }

  _handleStartShouldSetPanResponder = (e: Object, gestureState: Object) => {
    // 사용자가 원을 눌렀을 때 활성화되어야 할까?
    return true;
  }

  _handleMoveShouldSetPanResponder = (e: Object, gestureState: Object) => {
    //사용자가 터치한 채로 원 위를 지나갈 때 활성화되어야 할까?
    return true;
  }

  _handlePanResponderGrant = (e: Object, gestureState: Object) => {
    this._highlight();
  }

  _handlePanResponderMove = (e: Object, gestureState: Object) => {
    this.setState({
      stateID: gestureState.stateID,
      moveX: gestureState.moveX,
      moveY: gestureState.moveY,
      x0: gestureState.x0,
      y0: gestureState.y0,
      dx: gestureState.dx,
      dy: gestureState.dy,
      vx: gestureState.vx,
      vy: gestureState.vy,
      numberActiveTouches: gestureState.numberActiveTouches
    });


    //dx, dy를 이용하여 현재 위치를 계산한다

    if(gestureState.moveX > 185 && gestureState.moveX < 310)
    {
      this._circleStyles.style.left = this._previousLeft + gestureState.dx;

      var halfMoveX   =   gestureState.moveX - 185;

      var tanA =  ((halfMoveX) * Math.cos( ((halfMoveX) * (90 / 250)) * Math.PI /180));

      this._debugTanA  = tanA;
      this._circleStyles.style.top  =  100 - tanA;
    }else if(gestureState.moveX >= 310){
      this._circleStyles.style.left = this._previousLeft + 155;

      this._circleStyles.style.top  = 17;
    }else{
      this._circleStyles.style.left = this._previousLeft + gestureState.dx;

      this._circleStyles.style.top  = this._previousTop;
    }

    this._updatePosition();
  }

  _handlePanResponderEnd  = (e: Object, gestureState: Object) => {
    if(gestureState.moveX > 320)
    {

      distoryAdview();
      return;
    }

    this._unHighlight();
    this._previousLeft  =  -20;
    this._previousTop   =  100;


    this._circleStyles  = {
      style: {
        left: -20,
        top: 100,
      }
    };
    this._updatePosition();
  }
}


const styles = StyleSheet.create({
  circle: {
    width: CIRCLE_SIZE,
    height: CIRCLE_SIZE,
    borderRadius: CIRCLE_SIZE / 2,
    backgroundColor: CIRCLE_COLOR,
    position: 'absolute',
    left:0,
    top:0,
  },
  container: {
    flex:1,
    paddingTop:64,
  },
  debug: {
    position: 'absolute',
    left:-100,
    top:301,
  },
});

export default Unlock;
