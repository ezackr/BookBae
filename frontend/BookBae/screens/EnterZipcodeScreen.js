import React from 'react';
import { SafeAreaView, StyleSheet, Text, TextInput, Pressable } from 'react-native';

const EnterZipcodeScreen = ({navigation}) => {

    const [text, onChangeText] = React.useState(null);

    return (
        <SafeAreaView style={styles.container}>
            <Text>Enter Your Zipcode</Text>
            <TextInput style={styles.input} multiline={true} onChangeText={onChangeText} value={text} placeholder="xxxxx"/>
            <Pressable
                style={styles.button}
                onPress={() => navigation.navigate('EnterGenreScreen')}>
                <Text style={styles.buttonText}>Next</Text>
             </Pressable>
        </SafeAreaView>
    );

}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
  },
  button: {
      alignItems: 'center',
      justifyContent: 'center',
      paddingVertical: 12,
      paddingHorizontal: 32,
      borderRadius: 4,
      elevation: 3,
      backgroundColor: '#BD2A2A',
      marginTop: 5,
      marginBottom: 5,
  },
  buttonText: {
      fontSize: 16,
      lineHeight: 21,
      fontWeight: 'bold',
      letterSpacing: 0.25,
      color: 'white',
  },
});

export default EnterZipcodeScreen;