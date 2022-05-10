import React from 'react';
import {
  StyleSheet,
  Text,
  SafeAreaView,
  Pressable,
  Image,
  TouchableOpacity,
} from 'react-native';

const HomeScreen = ({navigation}) => {
  return (
    <SafeAreaView style={styles.container}>
      <SafeAreaView style={styles.topMenu}>
        <Text style={styles.title}>BookBae</Text>
      </SafeAreaView>
      <SafeAreaView style={styles.matchMenu}>
        <ProfileOverview />
      </SafeAreaView>
      <SafeAreaView style={styles.bottomMenu}>
        <TouchableOpacity style={styles.button}>
          <Image
            source="../images/deny.png"
            style={styles.button}
            resizeMode="stretch"
          />
        </TouchableOpacity>
        <TouchableOpacity style={styles.button}>
          <Image
            source="../images/accept.png"
            style={styles.button}
            resizeMode="stretch"
          />
        </TouchableOpacity>
      </SafeAreaView>
    </SafeAreaView>
  );
};

export default HomeScreen;

const ProfileOverview = () => {
  return (
    <SafeAreaView style={matchStyles.match}>
      <Text>Hello World</Text>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
  },
  topMenu: {
    flex: 1,
    backgroundColor: 'red',
    flexDirection: 'row',
    justifyContent: 'center',
  },
  matchMenu: {
    flex: 5,
    backgroundColor: 'green',
  },
  bottomMenu: {
    flex: 1,
    backgroundColor: 'blue',
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    color: 'black',
    fontSize: 30,
    fontWeight: 'bold',
  },
  button: {
    color: 'white',
    margin: 10,
  },
});

const matchStyles = StyleSheet.create({
  match: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});
