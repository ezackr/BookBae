import React from 'react';
import { SafeAreaView, View, StyleSheet, Text, TextInput, Pressable, Image } from 'react-native';

const EnterBooksScreen = ({navigation}) => {

    var bookCover = 'https://www.pngitem.com/pimgs/m/19-191625_icon-plus-png-gray-plus-icon-png-transparent.png'

    const [text] = React.useState(null);

    //start suggesting book title options from text input
    //change bookCover to chosen book
    const onChangeText = () => {

    }

    //we probably don't need to do anything here, we can store books as they are added in onChangeText
    const onPress = () => {
        console.log(bookCover)
        navigation.navigate('EnterPhotoScreen')
    }

    return (
        <SafeAreaView style={styles.container}>
            <Text style={styles.title}>Start Typing a Book Title:</Text>
            <TextInput style={styles.input} multiline={true} onChangeText={onChangeText} value={text} placeholder="xxxxx"/>
            <View style={styles.imageContainer}>
                <Image style={styles.image} source={{uri:bookCover}}/>
            </View>
            <Pressable
                style={styles.button}
                onPress={onPress}>
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
  input: {
    height: 40,
    width: '70%',
    margin: 1,
    borderWidth: 1,
    padding: 10,
  },
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
  },
  imageContainer: {
    paddingTop: 5,
    paddingBottom: 5,
  },
  image: {
    width: 200,
    height: 200,
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

export default EnterBooksScreen;