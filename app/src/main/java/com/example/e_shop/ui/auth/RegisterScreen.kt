package com.example.e_shop.ui.auth

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.e_shop.R
import com.example.e_shop.ui.common.ToastHelper
import com.example.e_shop.ui.components.AppText
import com.example.e_shop.ui.navigation.Screen
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

import androidx.compose.ui.res.stringResource

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var avatarUrl by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = LocalContext.current as? Activity

    val startForProfileImageResult = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val resultCode = result.resultCode
        val data = result.data

        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            if (fileUri != null) {
                selectedImageUri = fileUri
                
                // Process the file for upload
                val cacheFile = File(context.cacheDir, "upload_avatar_${System.currentTimeMillis()}.jpg")
                context.contentResolver.openInputStream(fileUri)?.use { input ->
                    FileOutputStream(cacheFile).use { output ->
                        input.copyTo(output)
                    }
                }
                
                val requestFile = cacheFile.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", cacheFile.name, requestFile)
                viewModel.uploadAvatar(body)
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            ToastHelper.showToast(context, ImagePicker.getError(data))
        }
    }

    val imageUploadedSuccessMsg = stringResource(R.string.image_uploaded_success)
    LaunchedEffect(uiState.uploadSuccessUrl) {
        uiState.uploadSuccessUrl?.let {
            avatarUrl = it
            ToastHelper.showToast(context, imageUploadedSuccessMsg)
        }
    }

    val registrationSuccessMsg = stringResource(R.string.registration_success)
    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            ToastHelper.showToast(context, registrationSuccessMsg)
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Register.route) { inclusive = true }
            }
            viewModel.resetState()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            ToastHelper.showToast(context, it)
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_large))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppText(text = stringResource(R.string.sign_up), style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_extra_large)))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(dimensionResource(R.dimen.avatar_size_large))
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Selected Avatar",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Button(
                    onClick = { 
                        if (activity != null) {
                            ImagePicker.with(activity)
                                .crop()
                                .createIntent { intent ->
                                    startForProfileImageResult.launch(intent)
                                }
                        }
                    },
                    shape = CircleShape,
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppText(text = stringResource(R.string.add_photo))
                }
            }
        }
        
        if (selectedImageUri != null) {
            TextButton(onClick = {
                if (activity != null) {
                    ImagePicker.with(activity)
                        .crop()
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }
                }
            }) {
                AppText(stringResource(R.string.change_photo))
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_xxl)))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { AppText(stringResource(R.string.name)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { AppText(stringResource(R.string.email)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { AppText(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_extra_large)))

        Button(
            onClick = { viewModel.register(name, email, password, avatarUrl) },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(dimensionResource(R.dimen.icon_size_small)), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                AppText(stringResource(R.string.register))
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_large)))

        TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
            AppText(stringResource(R.string.already_have_account))
        }
    }
}