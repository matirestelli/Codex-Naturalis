from PIL import Image
import os

def ritaglia_immagine(directory, coordinates):
    """
    Ritaglia un'immagine data le coordinate del rettangolo.

    Args:
    - input_path: Percorso dell'immagine originale.
    - output_path: Percorso dell'immagine ritagliata di output.
    - coordinates: Una tupla di coordinate (x0, y0, x1, y1) per il rettangolo da ritagliare.
    """

    for filename in os.listdir(directory):
        input_path = os.path.join(directory, filename)
        output_path = "CODEX/front_cropped/"f"ritagliata_{filename}"
        
        with Image.open(input_path) as img:
            img_cropped = img.crop(coordinates)
            img_cropped.save(output_path)
            print(f"Immagine salvata: {output_path}")

# Esempio di uso
directory = 'CODEX/front'
coordinates = (38, 38, 574, 408)  # Sostituisci con le tue coordinate (x0, y0, x1, y1)

ritaglia_immagine(directory, coordinates)