import React, {useState} from 'react';
import { SafeAreaView, StyleSheet, Text, TextInput, Pressable } from 'react-native';
import { Picker } from '@react-native-picker/picker'

const EnterGenreScreen = ({navigation}) => {

    const [selectedValue, setSelectedValue] = useState("java");

    return (
        <SafeAreaView style={styles.container}>
            <Text style={styles.title}>Enter Your Zipcode</Text>
            <Picker
                selectedValue={selectedValue}
                style={{ height: 50, width: 150 }}
                onValueChange={(itemValue, itemIndex) => setSelectedValue(itemValue)}>
                <Picker.Item label="Fantasy" value="java" />
                <Picker.Item label="Mystery" value="js" />
                <Picker.Item label="Science Fiction" value="js" />
                <Picker.Item label="Dystopian" value="js" />
                <Picker.Item label="Romance" value="js" />
                <Picker.Item label="Biography" value="js" />
                <Picker.Item label="History" value="js" />
                <Picker.Item label="True Crime" value="js" />
              </Picker>
            <Pressable
                style={styles.button}
                onPress={() => navigation.navigate('EnterBooksScreen')}>
                <Text style={styles.buttonText}>Next</Text>
             </Pressable>
        </SafeAreaView>
    );

}

const styles = StyleSheet.create({
  title: {
    color: 'black',
    fontSize: 24,
    fontWeight: 'bold',
  },
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

export default EnterGenreScreen;