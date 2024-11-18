from setuptools import setup, Extension

module = Extension ('example', sources=['example.pyx'])

setup(
    name='Cython',
    version='1.0',
    author='craig',
    ext_modules=[module]
)
